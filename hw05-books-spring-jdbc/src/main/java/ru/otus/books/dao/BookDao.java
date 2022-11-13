package ru.otus.books.dao;

import ru.otus.books.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    long count();
    long insert(Book book);
    Optional<Book> getById(long id);
    List<Book> getAll();
    void deleteById(long id);
}
