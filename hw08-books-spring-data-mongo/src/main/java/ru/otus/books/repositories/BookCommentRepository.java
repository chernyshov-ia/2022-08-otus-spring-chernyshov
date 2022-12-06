package ru.otus.books.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.books.domain.BookComment;

@Repository
public interface BookCommentRepository extends MongoRepository<BookComment, String> {
}
