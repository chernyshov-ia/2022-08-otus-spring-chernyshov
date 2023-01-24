package ru.otus.books.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.books.domain.TempAuthor;

@Repository
public interface TempAuthorRepository extends CrudRepository<TempAuthor, Long> {
}
