package ru.otus.books.repositories;

import ru.otus.books.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> findAll();
    Optional<Author> findById(long id);
}
