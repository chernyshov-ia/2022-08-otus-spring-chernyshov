package ru.otus.books.services;

import ru.otus.books.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentService {
    List<BookComment> findByBookId(long bookId);
    Optional<BookComment> findById(long id);
    Optional<BookComment> updateTextById(long id, String text);
    void deleteById(long id);
    Optional<BookComment> create(String text, long bookId);
}
