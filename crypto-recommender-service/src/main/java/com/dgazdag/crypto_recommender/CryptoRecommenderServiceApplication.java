package com.dgazdag.crypto_recommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CryptoRecommenderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoRecommenderServiceApplication.class, args);
	}

}
