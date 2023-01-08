package ru.otus.books.services;


import ru.otus.books.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Optional<Genre> findById(long id);
    List<Genre> findAll();
}
