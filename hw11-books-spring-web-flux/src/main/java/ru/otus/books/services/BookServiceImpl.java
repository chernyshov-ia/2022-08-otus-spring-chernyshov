package ru.otus.books.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.books.domain.Book;
import ru.otus.books.exceptions.NotFoundException;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;
import ru.otus.books.rest.dto.BookDto;

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

    @Transactional
    @Override
    public BookDto save(BookDto book) {

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
