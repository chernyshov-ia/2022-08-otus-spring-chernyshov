package ru.otus.books.domain;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Author {
    private final long id;
    private final String name;

    @Override
    public String toString() {
        return String.format("%d. %s", getId(), getName());
    }
}
