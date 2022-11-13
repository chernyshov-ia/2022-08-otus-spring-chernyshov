package ru.otus.books.services;

import org.springframework.stereotype.Service;
import ru.otus.books.dao.GenreDao;
import ru.otus.books.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Optional<Genre> getById(long id) {
        return genreDao.getById(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

}
