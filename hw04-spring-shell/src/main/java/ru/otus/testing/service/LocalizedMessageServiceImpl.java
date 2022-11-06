package ru.otus.testing.service;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class LocalizedMessageServiceImpl implements LocalizedMessageService {
    private final MessageSource messageSource;
    private final Locale locale;

    public LocalizedMessageServiceImpl(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    @Override
    public String getMessage(String code) {
        return messageSource.getMessage(code, new String[]{},
                "", locale);
    }
}
