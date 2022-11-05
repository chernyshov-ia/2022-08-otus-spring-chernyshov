package ru.otus.testing.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.testing.config.AppProps;

import java.util.Locale;

@Service
public class LocalizedMessageServiceImpl implements LocalizedMessageService {
    private final MessageSource messageSource;
    private final Locale locale;

    public LocalizedMessageServiceImpl(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public String getMessage(String code) {
        return messageSource.getMessage("runner.enterAnswer", new String[]{},
                "", locale);
    }
}
