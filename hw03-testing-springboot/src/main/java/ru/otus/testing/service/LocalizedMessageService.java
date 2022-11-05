package ru.otus.testing.service;

import java.util.Locale;

public interface LocalizedMessageService {
    Locale getLocale();
    String getMessage(String code);
}
