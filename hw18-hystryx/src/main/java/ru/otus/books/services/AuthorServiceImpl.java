package ru.otus.books.services;

import org.springframework.stereotype.Service;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.rest.dto.AuthorDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AuthorDto> findById(Long id) {
        return repository.findById(id)
                .map(AuthorDto::fromDomainObject);
    }

    private Optional<AuthorDto> emptyAuthorDtoOptionalFallback() {
        return Optional.empty();
    }

    @Override
    public List<AuthorDto> findAll() {
        return repository.findAll().stream()
                .map(AuthorDto::fromDomainObject)
                .collect(Collectors.toList());
    }

}
