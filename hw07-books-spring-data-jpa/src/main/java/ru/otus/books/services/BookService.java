package ru.otus.books.services;

import ru.otus.books.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);
    List<BookDto> findAll();
    void deleteById(long id);
    Optional<BookDto> create(String name, long authorId, long genreId);
    Optional<BookDto> updateName(long id, String name);
}
