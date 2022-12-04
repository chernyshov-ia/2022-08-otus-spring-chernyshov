package ru.otus.books.services;

import org.springframework.stereotype.Service;
import ru.otus.books.domain.Book;
import ru.otus.books.dto.BookDto;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookServiceImpl(BookRepository repository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional
    @Override
    public Optional<BookDto> findById(long id) {
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()) {
            return Optional.of(BookDto.fromBookWithComments(book.get()));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public List<BookDto> findAll() {
        return repository.findAll()
                .stream()
                .map(BookDto::fromBookWithoutComments)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public Optional<BookDto> create(String name, long authorId, long genreId) {

        if (isNull(name)) {
            return Optional.empty();
        }

        var author = authorRepository.findById(authorId);
        if (author.isEmpty()) {
            return Optional.empty();
        }

        var genre = genreRepository.findById(genreId);
        if (genre.isEmpty()) {
            return Optional.empty();
        }

        Book book = new Book(name, author.get(), genre.get(), null);

        return Optional.of(BookDto.fromBookWithoutComments(repository.save(book)));
    }

    @Transactional
    @Override
    public Optional<BookDto> updateName(long id, String name) {
        Optional<Book> optionalBook = repository.findById(id);
        if (optionalBook.isEmpty()) {
            return Optional.empty();
        }

        Book book = optionalBook.orElseThrow();

        book.setName(name);

        return Optional.of(BookDto.fromBookWithoutComments(repository.save(book)));
    }
}
