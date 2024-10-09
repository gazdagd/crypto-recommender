package com.dgazdag.crypto_recommender.listener;

import com.dgazdag.crypto_recommender.events.NewPriceAvailable;
import com.dgazdag.crypto_recommender.persistence.entities.DailySymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.repository.SymbolEvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.dgazdag.crypto_recommender.listener.CalculationUtil.createNewDaily;
import static com.dgazdag.crypto_recommender.listener.CalculationUtil.setNormalizedRange;
import static com.dgazdag.crypto_recommender.listener.CalculationUtil.toLocalDate;
import static com.dgazdag.crypto_recommender.listener.CalculationUtil.update;

@Component
@RequiredArgsConstructor
public class DailySymbolEvaluator {

    private final SymbolEvaluationRepository<DailySymbolEvaluation> dailySymbolEvaluationRepository;

    @EventListener
    public void newSymbolPriceAvailable(NewPriceAvailable newPriceAvailable) {
        var newSymbolEvaluation = dailySymbolEvaluationRepository.findBySymbolAndDate(newPriceAvailable.getSymbol(), toLocalDate(newPriceAvailable.getTime()))
                .map(symbolEvaluation -> update(newPriceAvailable, symbolEvaluation))
                .orElseGet(() -> createNewDaily(newPriceAvailable));
        setNormalizedRange(newSymbolEvaluation);
        dailySymbolEvaluationRepository.save(newSymbolEvaluation);
    }


}
