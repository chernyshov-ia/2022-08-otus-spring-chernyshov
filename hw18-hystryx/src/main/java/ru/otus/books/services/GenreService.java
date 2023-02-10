package ru.otus.books.services;


import ru.otus.books.rest.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<GenreDto> findAll();
}
