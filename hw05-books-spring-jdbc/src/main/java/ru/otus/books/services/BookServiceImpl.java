package ru.otus.books.services;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.books.dao.BookDao;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final IOService ioService;

    public BookServiceImpl(BookDao dao, IOService ioService) {
        this.bookDao = dao;
        this.ioService = ioService;
    }

    @Override
    public void list() {
        List<Book> books = bookDao.getAll();
        for (Book book : books) {
            ioService.outputString(book.toString());
        }
    }

    @Override
    public void deleteById(long id) {
        Book book;
        try {
            book = bookDao.getById(id);
        } catch ( EmptyResultDataAccessException e) {
            ioService.outputString("Book not found");
            return;
        }
        bookDao.deleteById(book.getId());
        ioService.outputString("DELETED: " + book.toString());
    }

    @Override
    public void create(String name, Author author, Genre genre) {
        var id = bookDao.insert(new Book(name, author, genre));
        var book = bookDao.getById(id);
        ioService.outputString("ADDED: " + book.toString());
    }

    @Override
    public Optional<Book> getById(long id) {
        Book book;
        try {
            book = bookDao.getById(id);
        } catch ( EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.of(book);
    }
}
