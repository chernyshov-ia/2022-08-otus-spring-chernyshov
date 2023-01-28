package ru.otus.books.services;

import ru.otus.books.rest.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Optional<AuthorDto> findById(Long id);
    List<AuthorDto> findAll();
}
