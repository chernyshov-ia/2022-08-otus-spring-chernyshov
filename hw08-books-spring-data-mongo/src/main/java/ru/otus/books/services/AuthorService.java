package ru.otus.books.services;

import ru.otus.books.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Optional<Author> findById(String id);
    List<Author> findAll();
}
