package ru.otus.books.dao;

import ru.otus.books.domain.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getAll();
    Genre getById(long id);
}
