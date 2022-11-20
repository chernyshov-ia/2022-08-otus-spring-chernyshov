package ru.otus.books.repositories;

import ru.otus.books.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> findAll();
    Optional<Genre> findById(long id);
}
