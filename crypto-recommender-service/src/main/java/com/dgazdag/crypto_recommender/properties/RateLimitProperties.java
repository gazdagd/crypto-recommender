package com.dgazdag.crypto_recommender.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "rate")
public class RateLimitProperties {
    private long limit;
    private int minutes;
}
