package com.dgazdag.crypto_recommender.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
    private String host;
    private int port;

}
