package com.dgazdag.crypto_recommender.listener;

import com.dgazdag.crypto_recommender.events.NewPriceAvailable;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolPrice;

import java.time.Instant;

public class TestData {
    public static final String SYMBOL = "BTC";
    public static final Instant NEW_TIME = Instant.parse("2022-01-01T04:00:00Z");
    public static final double NEW_PRICE = 43234.43;

    public static final SymbolPrice NEW_SYMBOL_PRICE = new SymbolPrice(NEW_PRICE, NEW_TIME);
    public static final SymbolPrice CURRENT_NEWEST = new SymbolPrice(NEW_PRICE + 10, NEW_TIME.plusSeconds(15));
    public static final SymbolPrice CURRENT_OLDEST = new SymbolPrice(NEW_PRICE + 10, NEW_TIME.minusSeconds(15));
    public static final SymbolPrice CURRENT_LOWEST = new SymbolPrice(NEW_PRICE - 10, NEW_TIME);
    public static final SymbolPrice CURRENT_HIGHEST = new SymbolPrice(NEW_PRICE + 10, NEW_TIME);

    public static final NewPriceAvailable NEW_PRICE_AVAILABLE = NewPriceAvailable.builder()
            .symbol(SYMBOL)
            .time(NEW_TIME)
            .price(NEW_PRICE)
            .build();
}
