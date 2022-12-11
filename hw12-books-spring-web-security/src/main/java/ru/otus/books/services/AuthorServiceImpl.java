package ru.otus.books.services;

import org.springframework.stereotype.Service;
import ru.otus.books.domain.Author;
import ru.otus.books.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Author> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return repository.findAll();
    }

}
