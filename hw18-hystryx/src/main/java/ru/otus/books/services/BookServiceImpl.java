package ru.otus.books.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import ru.otus.books.domain.Book;
import ru.otus.books.exceptions.NotFoundException;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;
import ru.otus.books.rest.dto.AuthorDto;
import ru.otus.books.rest.dto.BookDto;
import ru.otus.books.rest.dto.GenreDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @HystrixCommand(commandKey = "findBookById", fallbackMethod = "emptyBookDtoOptionalFallback")
    @Override
    public Optional<BookDto> findById(Long id) {
        var book = bookRepository.findById(id);
        if (book.isEmpty()) {
            return Optional.empty();
        } else {
            return book.map(BookDto::fromDomainObject);
        }
    }

    private Optional<BookDto> emptyBookDtoOptionalFallback() {
        return Optional.empty();
    }

    @HystrixCommand(commandKey = "findAllBooks", fallbackMethod = "noBooksDtoFallback")
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookDto::fromDomainObject)
                .collect(Collectors.toList());
    }

    private List<BookDto> noBooksDtoFallback() {
        return List.of();
    }

    @HystrixCommand(commandKey = "deleteBookById")
    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @HystrixCommand(commandKey = "saveBook",
            fallbackMethod = "saveFallback")
    @Transactional
    @Override
    public BookDto save(BookDto book) {
        if (book == null) {
            throw new IllegalArgumentException("book can't be null");
        }

        if (book.getAuthor() == null) {
            throw new IllegalArgumentException("Author can't be null");
        }

        if (book.getGenre() == null) {
            throw new IllegalArgumentException("Genre can't be null");
        }

        var genre = genreRepository.findById(book.getGenre().getId()).orElseThrow(NotFoundException::new);
        var author = authorRepository.findById(book.getAuthor().getId()).orElseThrow(NotFoundException::new);

        Book bookDomain;
        if (book.getId() == null) {
            bookDomain = new Book(book.getName(), author, genre);
        } else {
            bookDomain = bookRepository.findById(book.getId()).orElseThrow(NotFoundException::new);
            bookDomain.setName(book.getName());
            bookDomain.setAuthor(author);
            bookDomain.setGenre(genre);
        }

        return BookDto.fromDomainObject(bookRepository.save(bookDomain));
    }

    public BookDto saveFallback() {
        return new BookDto(0L, "", new AuthorDto(0L, ""), new GenreDto(0L, ""));
    }
}
