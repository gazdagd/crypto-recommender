package com.dgazdag.crypto_recommender.bootstrap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationStartedEvent;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BootstrapTest {
    @InjectMocks
    private Bootstrap bootstrap;
    @Mock
    private InitialDataReader initialDataReader;
    @Mock
    private ApplicationStartedEvent applicationStartedEvent;
    @Test
    void contextReady() {
        bootstrap.contextReady(applicationStartedEvent);
        verify(initialDataReader).readData();
    }
}