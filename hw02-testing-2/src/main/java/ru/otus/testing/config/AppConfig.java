package ru.otus.testing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.testing.service.IOService;
import ru.otus.testing.service.IOServiceStreams;

@Configuration
public class AppConfig {
    @Bean
    IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}
