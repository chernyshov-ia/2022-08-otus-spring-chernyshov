package ru.otus.books.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
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

    private Optional<GenreDto> emptyGenreDtoOptionalFallback() {
        return Optional.empty();
    }

    @HystrixCommand(commandKey = "findAllGenres", fallbackMethod = "noGenresDtoFallback")
    @Override
    public List<GenreDto> findAll() {
        return repository.findAll().stream()
                .map(GenreDto::fromDomainObject)
                .collect(Collectors.toList());
    }

    private List<GenreDto> noGenresDtoFallback() {
        return List.of();
    }

}
