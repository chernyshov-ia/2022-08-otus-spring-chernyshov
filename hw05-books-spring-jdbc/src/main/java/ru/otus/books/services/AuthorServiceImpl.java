package ru.otus.books.services;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.books.dao.AuthorDao;
import ru.otus.books.domain.Author;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;
    private final IOService ioService;

    public AuthorServiceImpl(AuthorDao authorDao, IOService ioService) {
        this.authorDao = authorDao;
        this.ioService = ioService;
    }

    @Override
    public Optional<Author> getById(long id) {
        Author author;
        try {
            author = authorDao.getById(id);
        } catch ( EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.of(author);
    }

    @Override
    public void list() {
        List<Author> authors = authorDao.getAll();
        for (Author author : authors) {
            ioService.outputString(author.toString());
        }
    }
}
