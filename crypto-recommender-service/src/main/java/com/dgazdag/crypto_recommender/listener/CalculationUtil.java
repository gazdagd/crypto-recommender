package com.dgazdag.crypto_recommender.listener;

import com.dgazdag.crypto_recommender.events.NewPriceAvailable;
import com.dgazdag.crypto_recommender.persistence.entities.DailySymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolPrice;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@UtilityClass
public class CalculationUtil {
    public static <T extends SymbolEvaluation> T update(NewPriceAvailable newPriceAvailable, T symbolEvaluation) {
        if(symbolEvaluation.getLowest().getPrice() > newPriceAvailable.getPrice()) {
            symbolEvaluation.setLowest(getSymbolPrice(newPriceAvailable));
        }
        if(symbolEvaluation.getHighest().getPrice() < newPriceAvailable.getPrice()) {
            symbolEvaluation.setHighest(getSymbolPrice(newPriceAvailable));
        }
        if(symbolEvaluation.getOldest().getTime().isAfter(newPriceAvailable.getTime())) {
            symbolEvaluation.setOldest(getSymbolPrice(newPriceAvailable));
        }
        if(symbolEvaluation.getNewest().getTime().isBefore(newPriceAvailable.getTime())) {
            symbolEvaluation.setNewest(getSymbolPrice(newPriceAvailable));
        }
        return symbolEvaluation;
    }

    public static SymbolEvaluation createNew(NewPriceAvailable newPriceAvailable) {
        var symbolPrice = getSymbolPrice(newPriceAvailable);
        return SymbolEvaluation.builder()
                .symbol(newPriceAvailable.getSymbol())
                .highest(symbolPrice)
                .lowest(symbolPrice)
                .newest(symbolPrice)
                .oldest(symbolPrice)
                .build();
    }

    public static DailySymbolEvaluation createNewDaily(NewPriceAvailable newPriceAvailable) {
        var symbolPrice = getSymbolPrice(newPriceAvailable);
        return DailySymbolEvaluation.builder()
                .symbol(newPriceAvailable.getSymbol())
                .highest(symbolPrice)
                .lowest(symbolPrice)
                .newest(symbolPrice)
                .oldest(symbolPrice)
                .date(toLocalDate(newPriceAvailable.getTime()))
                .build();
    }

    public static LocalDate toLocalDate(Instant instant) {
        return instant.atZone(ZoneId.of("UTC")).toLocalDate();
    }

    public static SymbolPrice getSymbolPrice(NewPriceAvailable newPriceAvailable) {
        return SymbolPrice.builder()
                .price(newPriceAvailable.getPrice())
                .time(newPriceAvailable.getTime())
                .build();
    }

    public static void setNormalizedRange(SymbolEvaluation symbolEvaluation) {
        symbolEvaluation.setNormalizedRange(
                (symbolEvaluation.getHighest().getPrice() - symbolEvaluation.getLowest().getPrice())
                        / symbolEvaluation.getLowest().getPrice());
    }
}
