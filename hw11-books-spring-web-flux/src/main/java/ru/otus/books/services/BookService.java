package ru.otus.books.services;

import ru.otus.books.rest.dto.BookDto;

public interface BookService {
    BookDto save(BookDto book);
}
