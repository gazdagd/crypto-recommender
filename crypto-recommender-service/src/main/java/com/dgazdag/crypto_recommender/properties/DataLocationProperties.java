package com.dgazdag.crypto_recommender.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "data")
public class DataLocationProperties {
    private String location;
}
