package com.dgazdag.crypto_recommender.listener;

import com.dgazdag.crypto_recommender.persistence.entities.SymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolPrice;
import com.dgazdag.crypto_recommender.persistence.repository.SymbolEvaluationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static com.dgazdag.crypto_recommender.listener.TestData.CURRENT_HIGHEST;
import static com.dgazdag.crypto_recommender.listener.TestData.CURRENT_LOWEST;
import static com.dgazdag.crypto_recommender.listener.TestData.CURRENT_NEWEST;
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
class GlobalSymbolEvaluatorTest {

    @InjectMocks
    GlobalSymbolEvaluator globalSymbolEvaluator;
    @Mock
    SymbolEvaluationRepository<SymbolEvaluation> symbolEvaluationRepository;
    @Captor
    ArgumentCaptor<SymbolEvaluation> symbolEvaluationArgumentCaptor;

    @Test
    void symbolPriceAvailableForNewSymbol() {
        when(symbolEvaluationRepository.findBySymbol(anyString()))
                .thenReturn(Optional.empty());

        globalSymbolEvaluator.newSymbolPriceAvailable(NEW_PRICE_AVAILABLE);

        verify(symbolEvaluationRepository).findBySymbol(SYMBOL);
        verify(symbolEvaluationRepository).save(symbolEvaluationArgumentCaptor.capture());
        var saved = symbolEvaluationArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(SYMBOL, saved.getSymbol()),
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
    void existingSymbolNoChange() {
        when(symbolEvaluationRepository.findBySymbol(anyString()))
                .thenReturn(Optional.of(SymbolEvaluation.builder()
                        .symbol(SYMBOL)
                        .highest(new SymbolPrice(NEW_PRICE + 10, NEW_TIME))
                        .lowest(new SymbolPrice(NEW_PRICE - 10, NEW_TIME))
                        .newest(new SymbolPrice(NEW_PRICE, NEW_TIME.plusSeconds(15)))
                        .oldest(new SymbolPrice(NEW_PRICE, NEW_TIME.minusSeconds(15)))
                        .build()));

        globalSymbolEvaluator.newSymbolPriceAvailable(NEW_PRICE_AVAILABLE);

        verify(symbolEvaluationRepository).save(any());
    }

    @MethodSource("argsProvider")
    @ParameterizedTest(name = "{index} {0}")
    void updateEvaluation(String displayName, SymbolEvaluation oldEvaluation, SymbolEvaluation expectedEvaluation) {
        when(symbolEvaluationRepository.findBySymbol(anyString()))
                .thenReturn(Optional.of(oldEvaluation));

        globalSymbolEvaluator.newSymbolPriceAvailable(NEW_PRICE_AVAILABLE);

        verify(symbolEvaluationRepository).save(symbolEvaluationArgumentCaptor.capture());
        var saved = symbolEvaluationArgumentCaptor.getValue();
        System.out.println(saved.getNormalizedRange());
        assertEquals(expectedEvaluation, saved);
    }

    private static Stream<Arguments> argsProvider() {
        return Stream.of(
                Arguments.of(
                        "Update Newest",
                        SymbolEvaluation.builder()
                                .symbol(SYMBOL)
                                .highest(CURRENT_HIGHEST)
                                .lowest(CURRENT_LOWEST)
                                .newest(new SymbolPrice(NEW_PRICE + 10, NEW_TIME.minusSeconds(10)))
                                .oldest(CURRENT_OLDEST)
                                .build(),
                        SymbolEvaluation.builder()
                                .symbol(SYMBOL)
                                .highest(CURRENT_HIGHEST)
                                .lowest(CURRENT_LOWEST)
                                .newest(NEW_SYMBOL_PRICE)
                                .oldest(CURRENT_OLDEST)
                                .normalizedRange(4.627013010929236E-4)
                                .build()),
                Arguments.of(
                        "Update Oldest",
                        SymbolEvaluation.builder()
                                .symbol(SYMBOL)
                                .highest(CURRENT_HIGHEST)
                                .lowest(CURRENT_LOWEST)
                                .newest(CURRENT_NEWEST)
                                .oldest(new SymbolPrice(NEW_PRICE + 10, NEW_TIME.plusSeconds(5)))
                                .build(),
                        SymbolEvaluation.builder()
                                .symbol(SYMBOL)
                                .highest(CURRENT_HIGHEST)
                                .lowest(CURRENT_LOWEST)
                                .newest(CURRENT_NEWEST)
                                .oldest(NEW_SYMBOL_PRICE)
                                .normalizedRange(4.627013010929236E-4)
                                .build()),
                Arguments.of(
                        "Update Highest",
                        SymbolEvaluation.builder()
                                .symbol(SYMBOL)
                                .highest(new SymbolPrice(NEW_PRICE - 10, NEW_TIME.minusSeconds(10)))
                                .lowest(CURRENT_LOWEST)
                                .newest(CURRENT_NEWEST)
                                .oldest(CURRENT_OLDEST)
                                .build(),
                        SymbolEvaluation.builder()
                                .symbol(SYMBOL)
                                .highest(NEW_SYMBOL_PRICE)
                                .lowest(CURRENT_LOWEST)
                                .newest(CURRENT_NEWEST)
                                .oldest(CURRENT_OLDEST)
                                .normalizedRange(2.313506505464618E-4)
                                .build()),
                Arguments.of(
                        "Update Lowest",
                        SymbolEvaluation.builder()
                                .symbol(SYMBOL)
                                .highest(CURRENT_HIGHEST)
                                .lowest(new SymbolPrice(NEW_PRICE + 10, NEW_TIME.minusSeconds(10)))
                                .newest(CURRENT_NEWEST)
                                .oldest(CURRENT_OLDEST)
                                .build(),
                        SymbolEvaluation.builder()
                                .symbol(SYMBOL)
                                .highest(CURRENT_HIGHEST)
                                .lowest(NEW_SYMBOL_PRICE)
                                .newest(CURRENT_NEWEST)
                                .oldest(CURRENT_OLDEST)
                                .normalizedRange(2.3129713980269892E-4)
                                .build())
        );
    }
}