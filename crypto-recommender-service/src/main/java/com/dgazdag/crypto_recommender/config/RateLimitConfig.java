package com.dgazdag.crypto_recommender.config;

import com.dgazdag.crypto_recommender.properties.RateLimitProperties;
import com.dgazdag.crypto_recommender.properties.RedisProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
public class RateLimitConfig {
    @Bean
    public RedisClient redisClient(RedisProperties redisProperties) {
        return RedisClient.create(RedisURI.builder()
                .withHost(redisProperties.getHost())
                .withPort(redisProperties.getPort())
                .withSsl(false)
                .build());
    }

    @Bean
    StatefulRedisConnection<String, byte[]> redisConnection(RedisClient redisClient) {
        return redisClient
                .connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }

    @Bean
    public ProxyManager<String> lettuceBasedProxyManager(StatefulRedisConnection<String, byte[]> redisConnection) {
        return LettuceBasedProxyManager.builderFor(redisConnection)
                .withExpirationStrategy(
                        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(1L)))
                .build();
    }

    @Bean
    public Supplier<BucketConfiguration> bucketConfiguration(RateLimitProperties rateLimitProperties) {
        return () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(rateLimitProperties.getLimit(),
                        Duration.ofMinutes(rateLimitProperties.getMinutes())))
                .build();
    }

}
