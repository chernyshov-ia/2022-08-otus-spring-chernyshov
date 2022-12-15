package ru.otus.books.services;

import ru.otus.books.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
}
