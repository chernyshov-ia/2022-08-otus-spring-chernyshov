package ru.otus.books.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;
import ru.otus.books.exceptions.InvalidAttributeException;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.rest.dto.BookDto;
import ru.otus.books.rest.dto.BookRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BookRestController {
    private final BookRepository bookRepository;

    public BookRestController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/api/v1/books")
    Flux<BookDto> books() {
        return bookRepository.findAll()
                .map(BookDto::fromDomainObject);
    }

    @GetMapping("/api/v1/books/{id}")
    Mono<ResponseEntity<BookDto>> getBook(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .map(BookDto::fromDomainObject)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/v1/books/{id}")
    Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return bookRepository.deleteById(id)
                .map(ResponseEntity::ok);
    }

    @PutMapping(path = "/api/v1/books/{id}")
    Mono<ResponseEntity<BookDto>> putBook(@NotNull @PathVariable("id") String id, @Valid @RequestBody BookRequestDto request) {
        return bookRepository
                .findById(id)
                .map(b -> {
                    b.setName(request.getName());
                    b.setGenre(new Genre(request.getGenreId()));
                    b.setAuthor(new Author(request.getAuthorId()));
                    return b;
                })
                .flatMap(bookRepository::save)
                .flatMap(b -> bookRepository.findById(b.getId()))
                .map(BookDto::fromDomainObject)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/api/v1/books")
    Mono<ResponseEntity<BookDto>> postBook(@Valid @RequestBody BookRequestDto request) {
        var book = new Book(null, request.getName(), new Author(request.getAuthorId()), new Genre(request.getGenreId()));
        return bookRepository.save(book)
                .flatMap(b -> bookRepository.findById(b.getId()))
                .map(BookDto::fromDomainObject)
                .map(ResponseEntity::ok);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAttributeException.class)
    public Map<String, String> handleValidationExceptions(InvalidAttributeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getAttributeName(), ex.getMessage());
        return errors;
    }
}
