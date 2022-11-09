package ru.otus.books.services;

import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;

import java.util.Optional;

public interface BookService {
    void list();
    Optional<Book> getById(long id);
    void deleteById(long id);
    void create(String name, Author author, Genre genre);
}
