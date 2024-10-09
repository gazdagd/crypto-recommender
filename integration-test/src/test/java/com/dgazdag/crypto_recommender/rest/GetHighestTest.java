package com.dgazdag.crypto_recommender.rest;

import com.dgazdag.crypto_recommender.IntegrationTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetHighestTest extends IntegrationTest {

    @Test
    void getHighest() throws Exception {
        mockMvc.perform(get("/api/v1/cryptos/highest?localDate=2022-01-01"))
                .andExpectAll(
                        status().isOk(),
                        content().json("""
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
                                }
                                 """)
                );
    }

    @Test
    void notFound() throws Exception {
        mockMvc.perform(get("/api/v1/cryptos/highest?localDate=2022-01-02"))
                .andExpectAll(
                        status().isNotFound(),
                        content().json("""
                                {
                                    "message": "There are no data for day 2022-01-02!"                                                       
                                }
                                """)
                );
    }

    @Test
    void parameterMissing() throws Exception {
        mockMvc.perform(get("/api/v1/cryptos/highest"))
                .andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                                {
                                    "message": "Required request parameter 'localDate' for method parameter type LocalDate is not present"                                                       
                                }
                                """)
                );
    }
}
