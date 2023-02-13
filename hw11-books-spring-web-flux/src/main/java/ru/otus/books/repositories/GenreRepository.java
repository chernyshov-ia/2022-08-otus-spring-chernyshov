package ru.otus.books.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.books.domain.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
}
