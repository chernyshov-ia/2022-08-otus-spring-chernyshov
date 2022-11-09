package ru.otus.books.services;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.books.dao.GenreDao;
import ru.otus.books.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;
    private final IOService ioService;

    public GenreServiceImpl(GenreDao genreDao, IOService ioService) {
        this.genreDao = genreDao;
        this.ioService = ioService;
    }

    @Override
    public Optional<Genre> getById(long id) {
        Genre genre;
        try {
            genre = genreDao.getById(id);
        } catch ( EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.of(genre);
    }

    @Override
    public void list() {
        List<Genre> authors = genreDao.getAll();
        for (Genre genre : authors) {
            ioService.outputString(genre.toString());
        }
    }
}
