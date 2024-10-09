package com.dgazdag.crypto_recommender.listener;

import com.dgazdag.crypto_recommender.persistence.entities.DailySymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolPrice;
import com.dgazdag.crypto_recommender.persistence.repository.SymbolEvaluationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.dgazdag.crypto_recommender.listener.TestData.CURRENT_HIGHEST;
import static com.dgazdag.crypto_recommender.listener.TestData.CURRENT_LOWEST;
import static com.dgazdag.crypto_recommender.listener.TestData.CURRENT_OLDEST;
import static com.dgazdag.crypto_recommender.listener.TestData.NEW_PRICE;
import static com.dgazdag.crypto_recommender.listener.TestData.NEW_PRICE_AVAILABLE;
import static com.dgazdag.crypto_recommender.listener.TestData.NEW_SYMBOL_PRICE;
import static com.dgazdag.crypto_recommender.listener.TestData.NEW_TIME;
import static com.dgazdag.crypto_recommender.listener.TestData.SYMBOL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailySymbolEvaluatorTest {


    public static final LocalDate DATE = LocalDate.of(2022, 1, 1);
    @InjectMocks
    DailySymbolEvaluator dailySymbolEvaluator;
    @Mock
    SymbolEvaluationRepository<DailySymbolEvaluation> dailySymbolEvaluationRepository;
    @Captor
    ArgumentCaptor<DailySymbolEvaluation> dailySymbolEvaluationArgumentCaptor;

    @Test
    void symbolPriceAvailableForNewSymbol() {
        when(dailySymbolEvaluationRepository.findBySymbolAndDate(anyString(), any()))
                .thenReturn(Optional.empty());

        dailySymbolEvaluator.newSymbolPriceAvailable(NEW_PRICE_AVAILABLE);

        verify(dailySymbolEvaluationRepository).findBySymbolAndDate(SYMBOL, DATE);
        verify(dailySymbolEvaluationRepository).save(dailySymbolEvaluationArgumentCaptor.capture());
        var saved = dailySymbolEvaluationArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(SYMBOL, saved.getSymbol()),
                () -> assertEquals(DATE, saved.getDate()),
                () -> assertEquals(NEW_TIME, saved.getOldest().getTime()),
                () -> assertEquals(NEW_PRICE, saved.getOldest().getPrice()),
                () -> assertEquals(NEW_TIME, saved.getNewest().getTime()),
                () -> assertEquals(NEW_PRICE, saved.getNewest().getPrice()),
                () -> assertEquals(NEW_TIME, saved.getHighest().getTime()),
                () -> assertEquals(NEW_PRICE, saved.getHighest().getPrice()),
                () -> assertEquals(NEW_TIME, saved.getLowest().getTime()),
                () -> assertEquals(NEW_PRICE, saved.getLowest().getPrice()),
                () -> assertEquals(0., saved.getNormalizedRange())
        );
    }

    @Test
    void updateEvaluation() {
        when(dailySymbolEvaluationRepository.findBySymbolAndDate(anyString(), any()))
                .thenReturn(Optional.of(DailySymbolEvaluation.builder()
                        .symbol(SYMBOL)
                        .highest(CURRENT_HIGHEST)
                        .lowest(CURRENT_LOWEST)
                        .newest(new SymbolPrice(NEW_PRICE + 10, NEW_TIME.minusSeconds(10)))
                        .oldest(CURRENT_OLDEST)
                        .date(DATE)
                        .build()));

        dailySymbolEvaluator.newSymbolPriceAvailable(NEW_PRICE_AVAILABLE);

        verify(dailySymbolEvaluationRepository).findBySymbolAndDate(SYMBOL, DATE);
        verify(dailySymbolEvaluationRepository).save(dailySymbolEvaluationArgumentCaptor.capture());
        var saved = dailySymbolEvaluationArgumentCaptor.getValue();
        assertEquals(DailySymbolEvaluation.builder()
                .symbol(SYMBOL)
                .highest(CURRENT_HIGHEST)
                .lowest(CURRENT_LOWEST)
                .newest(NEW_SYMBOL_PRICE)
                .oldest(CURRENT_OLDEST)
                .date(DATE)
                .normalizedRange(4.627013010929236E-4)
                .build(), saved);
    }
}