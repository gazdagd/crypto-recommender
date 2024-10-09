package com.dgazdag.crypto_recommender.mapper;

import com.dgazdag.crypto_recommender.bootstrap.dto.SymbolPriceCsv;
import com.dgazdag.crypto_recommender.events.NewPriceAvailable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SymbolPriceCsvMapper {

    @Mapping(target = "time", expression = "java(java.time.Instant.ofEpochMilli(Long.parseLong(symbolPriceCsv.getTime())))")
    NewPriceAvailable map(SymbolPriceCsv symbolPriceCsv);

}
