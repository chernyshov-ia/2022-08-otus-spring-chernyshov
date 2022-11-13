package ru.otus.books.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.books.services.IOService;
import ru.otus.books.services.IOServiceStreams;

@Configuration
public class AppConfig {
    @Bean
    IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}
