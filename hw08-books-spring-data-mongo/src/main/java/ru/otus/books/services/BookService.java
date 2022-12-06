package ru.otus.books.services;

import ru.otus.books.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(String id);
    List<BookDto> findAll();
    void deleteById(String id);
    Optional<BookDto> create(String name, String authorId, String genreId);
    Optional<BookDto> updateName(String id, String name);
}
