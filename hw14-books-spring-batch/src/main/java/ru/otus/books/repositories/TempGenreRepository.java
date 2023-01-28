package ru.otus.books.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.books.domain.TempGenre;

@Repository
public interface TempGenreRepository extends CrudRepository<TempGenre, Long> {
}
