package ru.otus.books.exceptions;

public class InvalidAttributeException extends RuntimeException{
    private final String attributeName;

    public String getAttributeName() {
        return attributeName;
    }

    public InvalidAttributeException(String attributeName, String message) {
        super(message);
        this.attributeName = attributeName;
    }
}
