package com.dgazdag.crypto_recommender.rest;

import com.dgazdag.crypto_recommender.IntegrationTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RateLimitTest extends IntegrationTest {

    @Test
    void tooManyRequests() throws Exception {
        mockMvc.perform(get("/api/v1/cryptos/highest?localDate=2022-01-01"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/cryptos/highest?localDate=2022-01-01"))
                .andExpectAll(
                        status().isTooManyRequests(),
                        header().string("X-Rate-Limit-Retry-After-Seconds", "59"),
                        content().json("""
                                {
                                    "message": "Too many requests"                                                       
                                }
                                """));
    }

}
