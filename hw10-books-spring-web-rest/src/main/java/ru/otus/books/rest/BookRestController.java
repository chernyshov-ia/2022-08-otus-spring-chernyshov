package ru.otus.books.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.books.rest.exceptions.InvalidAttributeException;
import ru.otus.books.rest.exceptions.NotFoundException;
import ru.otus.books.domain.Book;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;
import ru.otus.books.rest.dto.AuthorDto;
import ru.otus.books.rest.dto.BookDto;
import ru.otus.books.rest.dto.GenreDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class BookRestController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookRestController(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping("/books")
    List<BookDto> books() {
        var books = bookRepository.findAll().stream().map(BookDto::fromDomainObject).collect(Collectors.toList());
        return books;
    }

    @DeleteMapping("/book/{id}")
    ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/book/{id}")
    BookDto patchBook(@RequestBody BookDto book ) {
        if(book.getId() == null || book.getId() == 0) {
            throw new InvalidAttributeException("id", "Id не указано");
        }

        if(book.getName() == null || book.getName().length() <= 2) {
            throw new InvalidAttributeException("name", "Имя не указано или слишком короткое");
        }

        if(book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new InvalidAttributeException("author", "Автор не указан");
        }

        if(book.getGenre() == null || book.getGenre().getId() == null) {
            throw new InvalidAttributeException("genre", "Жанр не указан");
        }

        var genre = genreRepository.findById(book.getGenre().getId()).orElseThrow(() -> new NotFoundException("Жанр не найден"));
        var author = authorRepository.findById(book.getGenre().getId()).orElseThrow(() -> new NotFoundException("Автор не найден"));
        var bookDomain = bookRepository.findById(book.getId()).orElseThrow(NotFoundException::new);
        bookDomain.setName(book.getName());
        bookDomain.setAuthor(author);
        bookDomain.setGenre(genre);
        bookDomain = bookRepository.save(bookDomain);
        return BookDto.fromDomainObject(bookDomain);
    }

    @PostMapping("/book/{id}")
    BookDto postBook(@RequestBody BookDto book ) {
        if(book.getName() == null || book.getName().length() <= 2) {
            throw new InvalidAttributeException("name", "Имя не указано или слишком короткое");
        }

        if(book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new InvalidAttributeException("author", "Автор не указан");
        }

        if(book.getGenre() == null || book.getGenre().getId() == null) {
            throw new InvalidAttributeException("genre", "Жанр не указан");
        }

        var genre = genreRepository.findById(book.getGenre().getId()).orElseThrow(() -> new NotFoundException("Жанр не найден"));
        var author = authorRepository.findById(book.getGenre().getId()).orElseThrow(() -> new NotFoundException("Автор не найден"));
        var bookDomain = new Book(book.getName(), author, genre, List.of());
        bookDomain = bookRepository.save(bookDomain);
        return BookDto.fromDomainObject(bookDomain);
    }

    @GetMapping("/genres")
    List<GenreDto> genres() {
        var list = genreRepository.findAll().stream().map(GenreDto::fromDomainObject).collect(Collectors.toList());
        return list;
    }

    @GetMapping("/authors")
    List<AuthorDto> authors() {
        var list = authorRepository.findAll().stream().map(AuthorDto::fromDomainObject).collect(Collectors.toList());
        return list;
    }

}
