package ru.otus.books.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.books.domain.TempBook;

@Repository
public interface TempBookRepository extends CrudRepository<TempBook, Long> {
}
