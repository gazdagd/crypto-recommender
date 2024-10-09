package com.dgazdag.crypto_recommender.bootstrap;

import com.dgazdag.crypto_recommender.bootstrap.dto.SymbolPriceCsv;
import com.dgazdag.crypto_recommender.events.NewPriceAvailable;
import com.dgazdag.crypto_recommender.mapper.SymbolPriceCsvMapper;
import com.dgazdag.crypto_recommender.properties.DataLocationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvDataReaderTest {

    @InjectMocks
    private CsvDataReader csvDataReader;
    @Mock
    private DataLocationProperties dataLocationProperties;
    @Mock
    private SymbolPriceCsvMapper symbolPriceCsvMapper;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Captor
    ArgumentCaptor<SymbolPriceCsv> symbolPriceCsvArgumentCaptor;

    @Test
    void readData() {
        when(dataLocationProperties.getLocation()).thenReturn("test-data");
        when(symbolPriceCsvMapper.map(any()))
                .thenAnswer(invocationOnMock -> NewPriceAvailable.builder()
                        .symbol("BTC")
                        .price(3.4)
                        .time(Instant.parse("2022-01-01T04:00:00.000Z"))
                        .build());

        csvDataReader.readData();

        verify(dataLocationProperties).getLocation();
        verify(symbolPriceCsvMapper, times(2)).map(symbolPriceCsvArgumentCaptor.capture());
        verify(applicationEventPublisher, times(2)).publishEvent(any(NewPriceAvailable.class));
        List<SymbolPriceCsv> prices = symbolPriceCsvArgumentCaptor.getAllValues();
        assertIterableEquals(List.of(
                new SymbolPriceCsv("BTC", 46813.21, "1641009600000"),
                new SymbolPriceCsv("ETH", 3715.32, "1641024000000")),
                prices);

    }

}