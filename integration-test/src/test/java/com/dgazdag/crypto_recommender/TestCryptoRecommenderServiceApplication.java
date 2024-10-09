package com.dgazdag.crypto_recommender;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.SpringApplication;

public class TestCryptoRecommenderServiceApplication {

	static {
		var redisContainer = new RedisContainer(
				RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));
		redisContainer.start();
		System.setProperty("redis.host", redisContainer.getHost());
		System.setProperty("redis.port", redisContainer.getMappedPort(6379).toString());
	}

	public static void main(String[] args) {
		SpringApplication.from(CryptoRecommenderServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
