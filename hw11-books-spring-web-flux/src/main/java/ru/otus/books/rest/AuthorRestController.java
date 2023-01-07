package ru.otus.books.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.rest.dto.AuthorDto;

import java.util.List;

@RestController
public class AuthorRestController {
    private final AuthorRepository repository;

    public AuthorRestController(AuthorRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/v1/authors")
    Mono<List<AuthorDto>> getAuthors() {
        return repository.findAll()
                .map(AuthorDto::fromDomainObject)
                .collectList();
    }
}
