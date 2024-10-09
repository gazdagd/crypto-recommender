package com.dgazdag.crypto_recommender.events;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class NewPriceAvailable {
    String symbol;
    Double price;
    Instant time;
}
