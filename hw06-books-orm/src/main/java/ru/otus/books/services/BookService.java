package ru.otus.books.services;

import ru.otus.books.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(long id);
    List<Book> findAll();
    void deleteById(long id);
    Optional<Book> create(String name, long authorId, long genreId);
    Optional<Book> updateName(long id, String name);
}
