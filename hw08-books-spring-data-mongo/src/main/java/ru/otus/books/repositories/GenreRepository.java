package ru.otus.books.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.books.domain.Genre;

@Repository
public interface GenreRepository extends MongoRepository<Genre, String> {
}
