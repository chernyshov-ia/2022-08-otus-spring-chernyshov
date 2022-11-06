package ru.otus.testing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.testing.service.*;

@Configuration
@EnableConfigurationProperties(AppProps.class)
public class AppConfig {
    @Bean
    IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }

    @Bean
    QuestionsResourceProvider resourceProvider( AppProps props ) {
        return new FileQuestionsResourceProvider(props.getLocale(), props.getResourceFilename());
    }

    @Bean
    LocalizedMessageService localizedMessageService(MessageSource messageSource, AppProps props) {
        return new LocalizedMessageServiceImpl(messageSource, props.getLocale());
    }
}
