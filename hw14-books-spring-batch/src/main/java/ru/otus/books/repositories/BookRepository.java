package ru.otus.books.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.books.domain.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
}
