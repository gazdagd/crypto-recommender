package com.dgazdag.crypto_recommender;

import com.redis.testcontainers.RedisContainer;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;

// Super class for tests to use one context for faster test execution
@SpringBootTest
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public abstract class IntegrationTest {

    @Container
    private static final RedisContainer redisContainer = new RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));

    static {
        redisContainer.start();
    }

    @DynamicPropertySource
    static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("redis.host", redisContainer::getHost);
        registry.add("redis.port", () -> redisContainer.getMappedPort(6379).toString());
    }
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    StatefulRedisConnection<String, byte[]> redisConnection;

    @BeforeEach
    public void reset() {
        RedisCommands<String, byte[]> syncCommands = redisConnection.sync();
        syncCommands.flushall();
    }

}

