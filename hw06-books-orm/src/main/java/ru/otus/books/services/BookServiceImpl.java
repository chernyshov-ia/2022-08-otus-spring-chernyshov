package ru.otus.books.services;

import org.springframework.stereotype.Service;
import ru.otus.books.domain.Book;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Transactional
public class BookServiceImpl implements BookService{
    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookServiceImpl(BookRepository repository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Optional<Book> findById(long id) {
        Optional<Book> book = repository.findById(id);
        book.ifPresent(b -> b.getComments().size());
        return book;
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Book> create(String name, long authorId, long genreId) {

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

        return Optional.of(repository.save(book));
    }

    @Override
    public Optional<Book> updateName(long id, String name) {
        Optional<Book> optionalBook = repository.findById(id);
        if(optionalBook.isEmpty()) {
            return Optional.empty();
        }

        Book book = optionalBook.orElseThrow();

        book.setName(name);

        return Optional.of(repository.save(book));
    }
}
