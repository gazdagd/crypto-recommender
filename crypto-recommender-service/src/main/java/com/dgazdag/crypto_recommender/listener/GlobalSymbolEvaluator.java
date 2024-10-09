package com.dgazdag.crypto_recommender.listener;

import com.dgazdag.crypto_recommender.events.NewPriceAvailable;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.repository.SymbolEvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.dgazdag.crypto_recommender.listener.CalculationUtil.createNew;
import static com.dgazdag.crypto_recommender.listener.CalculationUtil.setNormalizedRange;
import static com.dgazdag.crypto_recommender.listener.CalculationUtil.update;

@Component
@RequiredArgsConstructor
public class GlobalSymbolEvaluator {

    private final SymbolEvaluationRepository<SymbolEvaluation> symbolEvaluationRepository;

    @EventListener
    public void newSymbolPriceAvailable(NewPriceAvailable newPriceAvailable) {
        var newSymbolEvaluation = symbolEvaluationRepository.findBySymbol(newPriceAvailable.getSymbol())
                .map(symbolEvaluation -> update(newPriceAvailable, symbolEvaluation))
                .orElseGet(() -> createNew(newPriceAvailable));
        setNormalizedRange(newSymbolEvaluation);
        symbolEvaluationRepository.save(newSymbolEvaluation);
    }
}
