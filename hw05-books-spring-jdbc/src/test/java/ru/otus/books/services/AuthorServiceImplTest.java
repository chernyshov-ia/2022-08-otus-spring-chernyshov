package ru.otus.books.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.books.dao.AuthorDao;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@DisplayName("сервис для работы с авторами должен")
@SpringBootTest
class AuthorServiceImplTest {
    private static final long NOT_EXISTING_AUTHOR_ID = 1000;
    private static final Author EXISTING_AUTHOR = new Author(3, "John Ronald Reuel Tolkien");

    @MockBean
    private IOService ioService;

    @MockBean
    private AuthorDao authorDao;

    private AuthorService authorService;

    @Configuration
    public static class NestedConfiguration { }

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImpl(authorDao, ioService);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        when(authorDao.getById(EXISTING_AUTHOR.getId())).thenReturn(EXISTING_AUTHOR);
        Optional<Author> actualAuthor = authorService.getById(EXISTING_AUTHOR.getId());
        assertThat(actualAuthor.get()).usingRecursiveComparison().isEqualTo(EXISTING_AUTHOR);
    }

    @DisplayName("возвращать Optional.Empty() по id при возникновении EmptyResultDataAccessException")
    @Test
    void shouldReturnEmptyByIdWhenThrowEmptyResultDataAccessException() {
        when(authorDao.getById(NOT_EXISTING_AUTHOR_ID)).thenThrow(EmptyResultDataAccessException.class);
        Optional<Author> actualAuthor = authorService.getById(NOT_EXISTING_AUTHOR_ID);
        assertThat(actualAuthor.isEmpty()).isTrue();
    }

    @DisplayName("выбрасывает исключение, если исключение не EmptyResultDataAccessException")
    @Test
    void shouldPassingUnexpectedExceptionWhenGetById() {
        doThrow(RuntimeException.class).when(authorDao).getById(anyLong());
        assertThatCode(() -> {
            Optional<Author> actualAuthor = authorService.getById(NOT_EXISTING_AUTHOR_ID);
        }).hasNoSuppressedExceptions();
    }

}