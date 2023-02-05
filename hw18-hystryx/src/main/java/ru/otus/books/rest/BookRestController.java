package ru.otus.books.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.otus.books.exceptions.InvalidAttributeException;
import ru.otus.books.rest.dto.AuthorDto;
import ru.otus.books.rest.dto.BookDto;
import ru.otus.books.rest.dto.BookRequestDto;
import ru.otus.books.rest.dto.GenreDto;
import ru.otus.books.services.BookService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BookRestController {
    private final BookService bookService;
    private final ResponseEntity serviceUnavailable =  ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @HystrixCommand(commandKey = "findAllBooks", fallbackMethod = "noBooksDtoFallback")
    @GetMapping("/api/v1/books")
    List<BookDto> books() {
        return bookService.findAll();
    }

    private List<BookDto> noBooksDtoFallback() {
        return List.of();
    }


    @HystrixCommand(commandKey = "findBookById", fallbackMethod = "getBookFallback")
    @GetMapping("/api/v1/books/{id}")
    ResponseEntity<BookDto> getBook(@PathVariable("id") Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    ResponseEntity<BookDto> getBookFallback(Long id) {
        return serviceUnavailable;
    }

    @HystrixCommand(commandKey = "deleteBookById")
    @DeleteMapping("/api/v1/books/{id}")
    ResponseEntity<String> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @HystrixCommand(commandKey = "updateBook", fallbackMethod = "getPutBookFallback")
    @PutMapping( path = "/api/v1/books/{id}")
    ResponseEntity<BookDto> putBook(@NotNull @PathVariable("id") Long id, @Valid @RequestBody BookRequestDto request) {
        var book = new BookDto().toBuilder()
                .id(id)
                .name(request.getName())
                .author(new AuthorDto(request.getAuthorId()))
                .genre(new GenreDto(request.getGenreId()))
                .build();

        return ResponseEntity.ok(bookService.save(book));
    }

    ResponseEntity<BookDto> getPutBookFallback(Long id, BookRequestDto request) {
        return serviceUnavailable;
    }


    @HystrixCommand(commandKey = "postBook", fallbackMethod = "getPostBookFallback")
    @PostMapping(path = "/api/v1/books")
    ResponseEntity<BookDto> postBook(@Valid @RequestBody BookRequestDto request) {
        var book = new BookDto().toBuilder()
                .name(request.getName())
                .author(new AuthorDto(request.getAuthorId()))
                .genre(new GenreDto(request.getGenreId()))
                .build();

        return ResponseEntity.ok(bookService.save(book));
    }

    ResponseEntity<BookDto> getPostBookFallback(BookRequestDto request) {
        return serviceUnavailable;
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
