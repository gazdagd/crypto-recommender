package com.dgazdag.crypto_recommender.service;

import com.dgazdag.crypto_recommender.exception.NotFoundException;
import com.dgazdag.crypto_recommender.persistence.entities.DailySymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.repository.SymbolEvaluationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SymbolEvaluationServiceImplTest {

    public static final String SYMBOL = "BTC";
    public static final LocalDate DATE = LocalDate.of(2022, 1, 1);
    @InjectMocks
    private SymbolEvaluationServiceImpl symbolEvaluationService;
    @Mock
    private SymbolEvaluationRepository<SymbolEvaluation> symbolEvaluationRepository;

    @Test
    void getAllGlobal() {
        List<SymbolEvaluation> symbolEvaluations = List.of();
        when(symbolEvaluationRepository.getAllGlobalOrderByNormalizedRangeDesc()).thenReturn(symbolEvaluations);

        assertEquals(symbolEvaluations, symbolEvaluationService.getAllGlobal());
    }

    @Test
    void getGlobalBySymbol() {
        SymbolEvaluation symbolEvaluation = SymbolEvaluation.builder().build();
        when(symbolEvaluationRepository.findBySymbol(anyString()))
                .thenReturn(Optional.of(symbolEvaluation));

        var result = symbolEvaluationService.getGlobalBySymbol(SYMBOL);

        verify(symbolEvaluationRepository).findBySymbol(SYMBOL);
        assertEquals(symbolEvaluation, result);
    }

    @Test
    void notFoundGlobalBySymbol() {
        when(symbolEvaluationRepository.findBySymbol(anyString()))
                .thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class, () -> symbolEvaluationService.getGlobalBySymbol(SYMBOL));
        assertEquals("Symbol BTC not found!", exception.getMessage());
    }

    @Test
    void getHighest() {
        DailySymbolEvaluation symbolEvaluation = DailySymbolEvaluation.builder().build();
        when(symbolEvaluationRepository.findHighestForDay(any()))
                .thenReturn(Optional.of(symbolEvaluation));

        var result = symbolEvaluationService.getHighest(DATE);

        verify(symbolEvaluationRepository).findHighestForDay(DATE);
        assertEquals(symbolEvaluation, result);
    }

    @Test
    void highestNotFound() {
        when(symbolEvaluationRepository.findHighestForDay(any()))
                .thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class, () -> symbolEvaluationService.getHighest(DATE));
        assertEquals("There are no data for day 2022-01-01!", exception.getMessage());
    }

}