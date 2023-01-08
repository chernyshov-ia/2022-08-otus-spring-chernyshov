package ru.otus.books.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.books.domain.User;
import ru.otus.books.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> userOptional = repository.findByUsername(username);
        userOptional.ifPresent(user -> user.getAuthorities().size());
        return userOptional;
    }
}
