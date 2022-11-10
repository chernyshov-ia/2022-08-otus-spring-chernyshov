package ru.otus.books.dao;

import ru.otus.books.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    List<Genre> getAll();
    Optional<Genre> getById(long id);
}
