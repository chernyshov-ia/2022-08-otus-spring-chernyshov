package ru.otus.books.services;

import ru.otus.books.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(Long id);
    List<BookDto> findAll();
    void deleteById(Long id);
    BookDto create(String name, Long authorId, Long genreId);
    BookDto update(Long id, String name, Long authorId, Long genreId);
}
