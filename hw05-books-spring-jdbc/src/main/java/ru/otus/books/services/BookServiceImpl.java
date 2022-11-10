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
    private final IOService ioService;

    public BookServiceImpl(BookDao dao, IOService ioService) {
        this.bookDao = dao;
        this.ioService = ioService;
    }

    @Override
    public void deleteById(long id) {
        bookDao.deleteById(id);
        ioService.outputString( String.format("Book{id=%d} deleted\n",id));
    }

    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }

    @Override
    public void create(String name, Author author, Genre genre) {
        var id = bookDao.insert(new Book(name, author, genre));
        var book = bookDao.getById(id);
        if(book.isPresent()) {
            ioService.outputString("ADDED: " + book.get());
        }
    }

    @Override
    public Optional<Book> getById(long id) {
        return bookDao.getById(id);
    }
}
