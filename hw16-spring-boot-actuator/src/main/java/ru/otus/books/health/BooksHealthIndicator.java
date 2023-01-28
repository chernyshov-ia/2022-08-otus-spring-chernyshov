package ru.otus.books.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.books.repositories.BookRepository;

@Component
public class BooksHealthIndicator implements HealthIndicator {

    private final BookRepository repository;

    public BooksHealthIndicator(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {
        if (repository.count() > 1) {
            return Health.up().build();
        } else {
            return Health.down().build();
        }
    }
}
