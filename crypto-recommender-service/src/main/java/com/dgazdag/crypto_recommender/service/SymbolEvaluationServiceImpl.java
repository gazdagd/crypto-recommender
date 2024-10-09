package com.dgazdag.crypto_recommender.service;

import com.dgazdag.crypto_recommender.exception.NotFoundException;
import com.dgazdag.crypto_recommender.persistence.entities.DailySymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.repository.SymbolEvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SymbolEvaluationServiceImpl implements SymbolEvaluationService {

    private final SymbolEvaluationRepository<SymbolEvaluation> symbolEvaluationRepository;

    @Override
    public List<SymbolEvaluation> getAllGlobal() {
        return symbolEvaluationRepository.getAllGlobalOrderByNormalizedRangeDesc();
    }

    @Override
    public SymbolEvaluation getGlobalBySymbol(String symbol) {
        return symbolEvaluationRepository.findBySymbol(symbol)
                .orElseThrow(() -> new NotFoundException("Symbol " + symbol + " not found!"));
    }

    @Override
    public DailySymbolEvaluation getHighest(LocalDate localDate) {
        return symbolEvaluationRepository.findHighestForDay(localDate)
                .orElseThrow(() -> new NotFoundException("There are no data for day " + localDate.toString() + "!"));
    }
}
