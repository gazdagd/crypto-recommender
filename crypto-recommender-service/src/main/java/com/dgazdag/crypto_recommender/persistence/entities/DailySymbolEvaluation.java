package com.dgazdag.crypto_recommender.persistence.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(DailySymbolEvaluation.DISCRIMINATOR)
public class DailySymbolEvaluation extends SymbolEvaluation {

    public static final String DISCRIMINATOR = "DAILY";

    private LocalDate date;
}
