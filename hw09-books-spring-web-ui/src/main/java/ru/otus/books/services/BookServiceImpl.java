package ru.otus.books.services;

import org.springframework.stereotype.Service;
import ru.otus.books.domain.Book;
import ru.otus.books.dto.BookDto;
import ru.otus.books.exceptions.NotFoundException;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;

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

    @Override
    public Optional<BookDto> findById(Long id) {
        var book = bookRepository.findById(id);
        if (book.isEmpty()) {
            return Optional.empty();
        } else {
            return book.map(BookDto::fromWithComments);
        }
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public BookDto save(BookDto book) {
        if(book == null) {
            throw new IllegalArgumentException("book can't be null");
        }
        var genre = genreRepository.findById(book.getGenreId()).orElseThrow(NotFoundException::new);
        var author = authorRepository.findById(book.getAuthorId()).orElseThrow(NotFoundException::new);

        Book bookDomain;
        if (book.getId() == null) {
            bookDomain = new Book(book.getName(), author, genre);
        } else {
            bookDomain = bookRepository.findById(book.getId()).orElseThrow(NotFoundException::new);
            bookDomain.setName(book.getName());
            bookDomain.setAuthor(author);
            bookDomain.setGenre(genre);
        }

        return BookDto.from(bookRepository.save(bookDomain));
    }
}
