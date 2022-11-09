package ru.otus.books.dao;

import ru.otus.books.domain.Author;

import java.util.List;

public interface AuthorDao {
    List<Author> getAll();
    Author getById(long id);
}
