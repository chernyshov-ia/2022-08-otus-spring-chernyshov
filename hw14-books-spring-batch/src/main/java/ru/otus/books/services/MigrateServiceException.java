package ru.otus.books.services;

public class MigrateServiceException extends RuntimeException {
    public MigrateServiceException(String message) {
        super(message);
    }

    public MigrateServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
