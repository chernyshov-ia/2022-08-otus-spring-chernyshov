package ru.otus.testing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.testing.service.IOService;
import ru.otus.testing.service.IOServiceStreams;

@Configuration
@EnableConfigurationProperties(AppProps.class)
public class AppConfig {
    @Bean
    IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}
