package ru.otus.books.services;

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

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public void deleteById(long id) {
        bookDao.deleteById(id);
    }

    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }

    @Override
    public Optional<Book> create(String name, Author author, Genre genre) {
        var id = bookDao.insert(new Book(name, author, genre));
        return bookDao.getById(id);
    }

    @Override
    public Optional<Book> getById(long id) {
        return bookDao.getById(id);
    }
}
