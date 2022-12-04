package ru.otus.books.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.BookComment;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCommentRepository extends JpaRepository<BookComment, Long> {
    List<BookComment> findByBookId(long bookId);
}
