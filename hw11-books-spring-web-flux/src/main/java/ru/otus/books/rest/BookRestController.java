package ru.otus.books.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.books.domain.Book;
import ru.otus.books.exceptions.InvalidAttributeException;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;
import ru.otus.books.rest.dto.AuthorDto;
import ru.otus.books.rest.dto.BookDto;
import ru.otus.books.rest.dto.BookRequestDto;
import ru.otus.books.rest.dto.GenreDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BookRestController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookRestController(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping("/api/v1/books")
    Mono<List<BookDto>> books() {
        return bookRepository.findAll()
                .map(BookDto::fromDomainObject)
                .collectList();
    }

    @GetMapping("/api/v1/books/{id}")
    Mono<ResponseEntity<BookDto>> getBook(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .map(BookDto::fromDomainObject)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/v1/books/{id}")
    Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return bookRepository.deleteById(id)
                .map(ResponseEntity::ok);
    }

    @PutMapping(path = "/api/v1/books/{id}")
    Mono<BookDto> putBook(@NotNull @PathVariable("id") String id, @Valid @RequestBody BookRequestDto request) {

        var genreMono = genreRepository.findById(request.getGenreId());
        var authorMono = authorRepository.findById(request.getAuthorId());


        Mono<Book> book = Mono.zip(genreMono, authorMono, (genre, author) -> new Book(request.getName(), author, genre));


//                .map(bookRepository::save);
//                .map(this::toDto);


    }

    @PostMapping(path = "/api/v1/books")
    BookDto postBook(@Valid @RequestBody BookRequestDto request) {
        var book = new BookDto().toBuilder()
                .name(request.getName())
                .author(new AuthorDto(request.getAuthorId()))
                .genre(new GenreDto(request.getGenreId()))
                .build();

        //    return bookService.save(book);
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

    BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getName(),
                AuthorDto.fromDomainObject(book.getAuthor()),
                GenreDto.fromDomainObject(book.getGenre()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAttributeException.class)
    public Map<String, String> handleValidationExceptions(InvalidAttributeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getAttributeName(), ex.getMessage());
        return errors;
    }
}
