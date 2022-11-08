package ru.otus.books.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Book {
    private final long id;
    private final String name;
    private final Author author;
    private final Genre genre;

    public Book(String name, Author author, Genre genre) {
        this.id = 0;
        this.name = name;
        this.author = author;
        this.genre = genre;
    }
}
