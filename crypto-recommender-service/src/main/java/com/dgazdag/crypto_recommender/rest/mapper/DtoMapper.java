package com.dgazdag.crypto_recommender.rest.mapper;

import com.dgazdag.crypto_recommender.persistence.entities.SymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolPrice;
import com.dgazdag.crypto_recommender.rest.dto.CryptoDto;
import com.dgazdag.crypto_recommender.rest.dto.CryptoPriceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper
public interface DtoMapper {

    default OffsetDateTime toOffsetDateTime(Instant instant){
        return instant.atOffset(ZoneOffset.UTC);
    }

    @Mapping(source = "time", target = "date")
    CryptoPriceDto mapPrice(SymbolPrice symbolPrice);

    List<CryptoDto> mapCryptoDtoList(List<SymbolEvaluation> symbolEvaluations);

    @Mapping(source = "highest", target = "max")
    @Mapping(source = "lowest", target = "min")
    @Mapping(source = "symbol", target = "name")
    CryptoDto mapCryptoDto(SymbolEvaluation symbolEvaluation);

}
