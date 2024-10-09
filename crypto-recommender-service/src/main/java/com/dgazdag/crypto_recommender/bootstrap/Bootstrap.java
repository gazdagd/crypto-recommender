package com.dgazdag.crypto_recommender.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Bootstrap {

    private final InitialDataReader initialDataReader;

    @EventListener
    public void contextReady(ApplicationStartedEvent applicationStartedEvent) {
        initialDataReader.readData();
    }

}
