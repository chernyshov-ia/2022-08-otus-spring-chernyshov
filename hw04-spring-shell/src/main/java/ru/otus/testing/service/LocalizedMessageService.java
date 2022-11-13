package ru.otus.testing.service;

public interface LocalizedMessageService {
    String getMessage(String code);
    String getMessage(String code, Object[] args);
}
