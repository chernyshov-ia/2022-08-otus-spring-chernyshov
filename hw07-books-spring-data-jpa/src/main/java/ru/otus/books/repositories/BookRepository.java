package ru.otus.books.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.books.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAll();
    Optional<Book> findById(long id);
    Book save(Book book);
    void deleteById(long id);
}
