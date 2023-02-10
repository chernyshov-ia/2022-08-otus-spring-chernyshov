package ru.otus.books.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.rest.dto.AuthorDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @HystrixCommand(commandKey = "findAllAuthors", fallbackMethod = "noAuthorsDtoFallback")
    @Override
    public List<AuthorDto> findAll() {
        return repository.findAll().stream()
                .map(AuthorDto::fromDomainObject)
                .collect(Collectors.toList());
    }

    private List<AuthorDto> noAuthorsDtoFallback() {
        return List.of();
    }

}
