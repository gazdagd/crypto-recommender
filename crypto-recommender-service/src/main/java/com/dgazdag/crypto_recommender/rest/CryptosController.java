package com.dgazdag.crypto_recommender.rest;

import com.dgazdag.crypto_recommender.rest.dto.CryptoDto;
import com.dgazdag.crypto_recommender.rest.mapper.DtoMapper;
import com.dgazdag.crypto_recommender.rest.api.CryptosApiDelegate;
import com.dgazdag.crypto_recommender.rest.dto.CryptoListDto;
import com.dgazdag.crypto_recommender.service.SymbolEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CryptosController implements CryptosApiDelegate {

    private final SymbolEvaluationService symbolEvaluationService;
    private final DtoMapper dtoMapper;

    @Override
    public CryptoListDto getAll() {
        return new CryptoListDto()
                .cryptos(dtoMapper.mapCryptoDtoList(symbolEvaluationService.getAllGlobal()));
    }

    @Override
    public CryptoDto getBySymbol(String symbol) {
        return dtoMapper.mapCryptoDto(symbolEvaluationService.getGlobalBySymbol(symbol));
    }

    @Override
    public CryptoDto getHighest(LocalDate localDate) {
        return dtoMapper.mapCryptoDto(symbolEvaluationService.getHighest(localDate));
    }
}
