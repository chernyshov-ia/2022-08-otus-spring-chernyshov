package ru.otus.books.services;

import ru.otus.books.rest.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(Long id);

    List<BookDto> findAll();

    void deleteById(Long id);

    BookDto save(BookDto book);
}
