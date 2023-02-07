package ru.otus.books.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.books.domain.Book;
import ru.otus.books.exceptions.NotFoundException;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;
import ru.otus.books.rest.dto.BookDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Qualifier("bookService")
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
            return book.map(BookDto::fromDomainObject);
        }
    }


    @Override
    public List<BookDto> findAll() {
        sleep(1500);
        return bookRepository.findAll().stream()
                .map(BookDto::fromDomainObject)
                .collect(Collectors.toList());
    }




    private void sleep(int m) {
        int i = ThreadLocalRandom.current().nextInt(m);
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

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
}
