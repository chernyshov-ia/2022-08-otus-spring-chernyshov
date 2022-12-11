package ru.otus.books.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.books.exceptions.InvalidAttributeException;
import ru.otus.books.exceptions.NotFoundException;
import ru.otus.books.rest.dto.AuthorDto;
import ru.otus.books.rest.dto.BookDto;
import ru.otus.books.rest.dto.GenreDto;
import ru.otus.books.services.AuthorService;
import ru.otus.books.services.BookService;
import ru.otus.books.services.GenreService;

import java.util.List;

@RestController
public class BookRestController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookRestController(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/api/v1/books")
    List<BookDto> books() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/book/{id}")
    BookDto getBook(@PathVariable("id") Long id) {
        return bookService.findById(id).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("/api/v1/book/{id}")
    ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping( path = "/api/v1/book/{id}")
    BookDto putBook(@PathVariable("id") Long id, @RequestBody BookDto book) {

        if (id == null || id == 0L) {
            throw new InvalidAttributeException("id", "Id не указано");
        }

        book.setId(id);

        if (book.getName() == null || book.getName().length() <= 2) {
            throw new InvalidAttributeException("name", "Имя не указано или слишком короткое");
        }

        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new InvalidAttributeException("author", "Автор не указан");
        }

        if (book.getGenre() == null || book.getGenre().getId() == null) {
            throw new InvalidAttributeException("genre", "Жанр не указан");
        }

        return bookService.save(book);
    }

    @PostMapping(path = "/api/v1/books")
    BookDto postBook(@RequestBody BookDto book) {
        if (book.getName() == null || book.getName().length() <= 2) {
            throw new InvalidAttributeException("name", "Имя не указано или слишком короткое");
        }

        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new InvalidAttributeException("author", "Автор не указан");
        }

        if (book.getGenre() == null || book.getGenre().getId() == null) {
            throw new InvalidAttributeException("genre", "Жанр не указан");
        }

        return bookService.save(book);
    }

    @GetMapping("/api/v1/genres")
    List<GenreDto> genres() {
        return genreService.findAll();
    }

    @GetMapping("/api/v1/authors")
    List<AuthorDto> authors() {
        return authorService.findAll();
    }

}
