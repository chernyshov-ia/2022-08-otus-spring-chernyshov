package ru.otus.books.services;

import ru.otus.books.domain.Author;
import ru.otus.books.domain.Genre;

public interface BookService {
    void list();
    void deleteById(long id);
    void create(String name, Author author, Genre genre);
}
