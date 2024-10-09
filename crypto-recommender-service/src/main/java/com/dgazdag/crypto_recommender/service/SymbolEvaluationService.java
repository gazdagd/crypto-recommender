package com.dgazdag.crypto_recommender.service;

import com.dgazdag.crypto_recommender.persistence.entities.DailySymbolEvaluation;
import com.dgazdag.crypto_recommender.persistence.entities.SymbolEvaluation;

import java.time.LocalDate;
import java.util.List;

public interface SymbolEvaluationService {

    List<SymbolEvaluation> getAllGlobal();

    SymbolEvaluation getGlobalBySymbol(String symbol);

    DailySymbolEvaluation getHighest(LocalDate localDate);

}
