package ru.otus.books.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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

    @HystrixCommand(commandKey = "findAllAuthors", fallbackMethod = "noAuthorsDtoFallback")
    @GetMapping("/api/v1/authors")
    List<AuthorDto> getAuthors() {
        return authorService.findAll();
    }

    private List<AuthorDto> noAuthorsDtoFallback() {
        return List.of();
    }
}
