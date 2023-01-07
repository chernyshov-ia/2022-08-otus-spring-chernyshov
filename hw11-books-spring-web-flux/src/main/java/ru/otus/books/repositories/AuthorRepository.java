package ru.otus.books.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.books.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
