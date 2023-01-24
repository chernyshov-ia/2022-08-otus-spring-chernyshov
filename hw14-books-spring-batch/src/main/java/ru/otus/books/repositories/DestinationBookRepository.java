package ru.otus.books.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.books.domain.DestinationBook;

public interface DestinationBookRepository extends JpaRepository<DestinationBook, Integer> {
}
