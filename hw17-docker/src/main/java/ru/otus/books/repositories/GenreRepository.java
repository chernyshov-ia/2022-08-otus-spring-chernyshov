package ru.otus.books.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.books.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
