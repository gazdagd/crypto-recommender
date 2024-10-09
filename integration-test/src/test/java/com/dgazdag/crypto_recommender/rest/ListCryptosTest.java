package com.dgazdag.crypto_recommender.rest;

import com.dgazdag.crypto_recommender.IntegrationTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ListCryptosTest extends IntegrationTest {

    @Test
    void listCryptos() throws Exception {
        mockMvc.perform(get("/api/v1/cryptos"))
                .andExpectAll(
                        status().isOk(),
                        header().string("X-Rate-Limit-Remaining", "0"),
                        content().json("""
                                {"cryptos": [
                                    {
                                        "name": "ETH",
                                        "normalizedRange": 0.008268777183909334,
                                        "oldest": {
                                            "date": "2022-01-01T08:00:00Z",
                                            "price": 3715.32
                                        },
                                        "newest": {
                                            "date": "2022-01-01T17:00:00Z",
                                            "price": 3727.61
                                        },
                                        "max": {
                                            "date": "2022-01-01T17:00:00Z",
                                            "price": 3727.61
                                        },
                                        "min": {
                                            "date": "2022-01-01T15:00:00Z",
                                            "price": 3697.04
                                        }                  
                                    },
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
                                ]}
                                """)
                );
    }


}
