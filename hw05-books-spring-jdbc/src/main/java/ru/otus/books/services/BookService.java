package ru.otus.books.services;

import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> getById(long id);
    List<Book> getAll();
    void deleteById(long id);
    void create(String name, Author author, Genre genre);
}
