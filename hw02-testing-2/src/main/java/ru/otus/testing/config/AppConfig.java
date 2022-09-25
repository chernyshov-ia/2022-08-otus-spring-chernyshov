package ru.otus.testing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.testing.service.IOService;
import ru.otus.testing.service.IOServiceStreams;

@PropertySource("classpath:application.properties")
@Configuration
public class AppConfig {
    @Bean
    IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}
