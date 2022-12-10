package ru.otus.books.rest.exceptions;

public class InvalidAttributeException extends RuntimeException{
    private final String attributeName;

    public InvalidAttributeException(String attributeName, String message) {
        super(message);
        this.attributeName = attributeName;
    }
}
