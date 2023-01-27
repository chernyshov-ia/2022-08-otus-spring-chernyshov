package ru.otus.books.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.books.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
