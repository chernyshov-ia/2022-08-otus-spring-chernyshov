package ru.otus.books.services;

import ru.otus.books.domain.Author;

import java.util.Optional;

public interface AuthorService {
    Optional<Author> getById(long id);
    void list();
}
