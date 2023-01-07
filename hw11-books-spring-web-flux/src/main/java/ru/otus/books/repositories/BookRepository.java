package ru.otus.books.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.books.domain.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
}
