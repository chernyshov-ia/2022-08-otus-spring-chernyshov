package ru.otus.books.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.books.rest.dto.AuthorDto;
import ru.otus.books.services.AuthorService;

import java.util.List;

@RestController
public class AuthorRestController {
    private final AuthorService authorService;

    public AuthorRestController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/api/v1/authors")
    List<AuthorDto> getAuthors() {
        return authorService.findAll();
    }
}
