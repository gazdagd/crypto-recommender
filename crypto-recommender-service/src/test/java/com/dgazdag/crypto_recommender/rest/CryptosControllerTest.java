package com.dgazdag.crypto_recommender.rest;

import com.dgazdag.crypto_recommender.persistence.entities.DailySymbolEvaluation;
import com.dgazdag.crypto_recommender.rest.mapper.DtoMapper;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolEvaluation;
import com.dgazdag.crypto_recommender.rest.dto.CryptoDto;
import com.dgazdag.crypto_recommender.service.SymbolEvaluationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptosControllerTest {

    public static final String SYMBOL = "BTC";
    public static final LocalDate DATE = LocalDate.of(2022, 1, 1);
    @InjectMocks
    private CryptosController cryptosController;
    @Mock
    private SymbolEvaluationService symbolEvaluationService;
    @Mock
    private DtoMapper dtoMapper;

    @Test
    void getAll() {
        List<SymbolEvaluation> symbolEvaluations = List.of(SymbolEvaluation.builder().build());
        when(symbolEvaluationService.getAllGlobal()).thenReturn(symbolEvaluations);
        List<CryptoDto> cryptoDtos = List.of(new CryptoDto());
        when(dtoMapper.mapCryptoDtoList(anyList())).thenReturn(cryptoDtos);

        assertEquals(cryptoDtos, cryptosController.getAll().getCryptos());

    }

    @Test
    void getBySymbol() {
        SymbolEvaluation symbolEvaluation = SymbolEvaluation.builder().build();
        when(symbolEvaluationService.getGlobalBySymbol(anyString()))
                .thenReturn(symbolEvaluation);
        CryptoDto cryptoDto = new CryptoDto();
        when(dtoMapper.mapCryptoDto(any()))
                .thenReturn(cryptoDto);

        var result = cryptosController.getBySymbol(SYMBOL);

        verify(dtoMapper).mapCryptoDto(symbolEvaluation);
        verify(symbolEvaluationService).getGlobalBySymbol(SYMBOL);
        assertEquals(cryptoDto, result);
    }

    @Test
    void getHighest() {
        DailySymbolEvaluation symbolEvaluation = DailySymbolEvaluation.builder().build();
        when(symbolEvaluationService.getHighest(any()))
                .thenReturn(symbolEvaluation);
        CryptoDto cryptoDto = new CryptoDto();
        when(dtoMapper.mapCryptoDto(any()))
                .thenReturn(cryptoDto);

        var result = cryptosController.getHighest(DATE);

        verify(dtoMapper).mapCryptoDto(symbolEvaluation);
        verify(symbolEvaluationService).getHighest(DATE);
        assertEquals(cryptoDto, result);
    }
}