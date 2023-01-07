package ru.otus.books.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.books.repositories.GenreRepository;
import ru.otus.books.rest.dto.GenreDto;

import java.util.List;

@RestController
public class GenreRestController {
    private final GenreRepository repository;

    public GenreRestController(GenreRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/v1/genres")
    Mono<List<GenreDto>> getGenres() {
        return repository.findAll()
                .map(GenreDto::fromDomainObject)
                .collectList();
    }
}
