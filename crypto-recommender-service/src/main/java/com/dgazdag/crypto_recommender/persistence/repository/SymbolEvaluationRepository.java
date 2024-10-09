package com.dgazdag.crypto_recommender.persistence.repository;

import com.dgazdag.crypto_recommender.persistence.entities.DailySymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SymbolEvaluationRepository<T extends SymbolEvaluation> extends JpaRepository<T, Long> {

    @Query("select se from SymbolEvaluation se where se.class = SymbolEvaluation and se.symbol = :symbol")
    Optional<SymbolEvaluation> findBySymbol(String symbol);

    Optional<DailySymbolEvaluation> findBySymbolAndDate(String symbol, LocalDate localDate);

    @Query("select se from SymbolEvaluation se where se.class = SymbolEvaluation order by se.normalizedRange desc")
    List<SymbolEvaluation> getAllGlobalOrderByNormalizedRangeDesc();

    @Query("select se from DailySymbolEvaluation se where se.date = :localDate " +
            "and se.normalizedRange = (select max(s.normalizedRange) from DailySymbolEvaluation s where s.date = :localDate )")
    List<DailySymbolEvaluation> findMaxRangeForDay(LocalDate localDate);

    default Optional<DailySymbolEvaluation> findHighestForDay(LocalDate localDate) {
        return findMaxRangeForDay(localDate)
                .stream()
                .findFirst(); // In case there are two or more result
    }

}
