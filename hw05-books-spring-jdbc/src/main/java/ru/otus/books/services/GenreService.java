package ru.otus.books.services;

import ru.otus.books.domain.Genre;

import java.util.Optional;

public interface GenreService {
    Optional<Genre> getById(long id);
    void list();
}
