package ru.otus.books.services;

import ru.otus.books.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentService {
    List<BookComment> findByBookId(String bookId);
    Optional<BookComment> findById(String id);
    Optional<BookComment> updateTextById(String id, String text);
    void deleteById(String id);
    Optional<BookComment> create(String text, String bookId);
}
