package com.dgazdag.crypto_recommender.bootstrap;

import com.dgazdag.crypto_recommender.bootstrap.dto.SymbolPriceCsv;
import com.dgazdag.crypto_recommender.exception.InitException;
import com.dgazdag.crypto_recommender.mapper.SymbolPriceCsvMapper;
import com.dgazdag.crypto_recommender.properties.DataLocationProperties;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsvDataReader implements InitialDataReader {

    private final DataLocationProperties dataLocationProperties;
    private final SymbolPriceCsvMapper symbolPriceCsvMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void readData() {
        try (Stream<Path> paths = Files.walk(getLocationPath())) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(this::parseFile);
        } catch (IOException e) {
            throw new InitException(e.getMessage());
        }
    }

    private void parseFile(Path file) {
        try (FileReader fileReader = new FileReader(file.toFile())) {
            new CsvToBeanBuilder<SymbolPriceCsv>(fileReader)
                    .withType(SymbolPriceCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .stream()
                    .map(symbolPriceCsvMapper::map)
                    .forEach(applicationEventPublisher::publishEvent);
        } catch (IOException e) {
            throw new InitException(e.getMessage());
        }
    }

    private Path getLocationPath() {
        try {
            return Path.of(ClassLoader.getSystemResource(dataLocationProperties.getLocation()).toURI());
        } catch (URISyntaxException e) {
            throw new InitException(e.getMessage());
        }
    }
}
