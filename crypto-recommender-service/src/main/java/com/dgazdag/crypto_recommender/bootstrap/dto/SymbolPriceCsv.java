package com.dgazdag.crypto_recommender.bootstrap.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymbolPriceCsv {
    @CsvBindByName
    private String symbol;

    @CsvBindByName
    private Double price;

    @CsvBindByName(column = "timestamp")
    private String time;
}
