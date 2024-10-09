package com.dgazdag.crypto_recommender.persistence.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DiscriminatorValue(SymbolEvaluation.DISCRIMINATOR)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SymbolEvaluation {

    public static final String DISCRIMINATOR = "GLOBAL";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "oldest_price"))
    @AttributeOverride(name = "time", column = @Column(name = "oldest_time"))
    private SymbolPrice oldest;
    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "newest_price"))
    @AttributeOverride(name = "time", column = @Column(name = "newest_time"))
    private SymbolPrice newest;
    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "highest_price"))
    @AttributeOverride(name = "time", column = @Column(name = "highest_time"))
    private SymbolPrice highest;
    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "lowest_price"))
    @AttributeOverride(name = "time", column = @Column(name = "lowest_time"))
    private SymbolPrice lowest;

    private Double normalizedRange;
}
