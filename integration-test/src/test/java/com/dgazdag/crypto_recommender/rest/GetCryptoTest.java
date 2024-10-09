package com.dgazdag.crypto_recommender.rest;

import com.dgazdag.crypto_recommender.IntegrationTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetCryptoTest extends IntegrationTest {

    @Test
    void getCrypto() throws Exception {
        mockMvc.perform(get("/api/v1/cryptos/BTC"))
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                                {
                                        "name": "BTC",
                                        "normalizedRange": 0.0035545522300222832,
                                        "oldest": {
                                            "date": "2022-01-01T04:00:00Z",
                                            "price": 46813.21
                                        },
                                        "newest": {
                                            "date": "2022-01-01T07:00:00Z",
                                            "price": 46979.61
                                        },
                                        "max": {
                                            "date": "2022-01-01T07:00:00Z",
                                            "price": 46979.61
                                        },
                                        "min": {
                                            "date": "2022-01-01T04:00:00Z",
                                            "price": 46813.21
                                        }                 
                                    }
                                """)
                );
    }

    @Test
    void notFound() throws Exception {
        mockMvc.perform(get("/api/v1/cryptos/XXX"))
                .andExpectAll(
                        status().isNotFound(),
                        content().json("""
                                {
                                    "message": "Symbol XXX not found!"                                                       
                                }
                                """)
                );
    }

}
