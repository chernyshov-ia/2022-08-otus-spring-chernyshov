package ru.otus.books.services;

import org.springframework.stereotype.Service;
import ru.otus.books.domain.Genre;
import ru.otus.books.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;

    public GenreServiceImpl(GenreRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Genre> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Genre> findAll() {
        return repository.findAll();
    }

}
