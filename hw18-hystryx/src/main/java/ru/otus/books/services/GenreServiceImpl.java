package ru.otus.books.services;

import org.springframework.stereotype.Service;
import ru.otus.books.domain.Genre;
import ru.otus.books.repositories.GenreRepository;
import ru.otus.books.rest.dto.GenreDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;

    public GenreServiceImpl(GenreRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<GenreDto> findById(Long id) {
        return repository.findById(id).map(GenreDto::fromDomainObject);
    }

    @Override
    public List<GenreDto> findAll() {
        return repository.findAll().stream()
                .map(GenreDto::fromDomainObject)
                .collect(Collectors.toList());
    }

}
