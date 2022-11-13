package ru.otus.books.dao;

import ru.otus.books.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    List<Author> getAll();
    Optional<Author> getById(long id);
}
