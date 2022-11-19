package ru.otus.books.repositories;

import ru.otus.books.domain.Book;
import ru.otus.books.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository {
    List<BookComment> findByBookId(long bookId);
    Optional<BookComment> findById(long id);
    BookComment save(BookComment comment);
    void deleteById(long id);
}
