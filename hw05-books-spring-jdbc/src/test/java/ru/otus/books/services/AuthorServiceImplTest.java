package ru.otus.books.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.books.dao.AuthorDao;
import ru.otus.books.domain.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("сервис для работы с авторами должен")
@SpringBootTest
class AuthorServiceImplTest {
    private static final long NOT_EXISTING_AUTHOR_ID = 1000;
    private static final Author EXISTING_AUTHOR = new Author(3, "John Ronald Reuel Tolkien");

    @MockBean
    private IOService ioService;

    @Autowired
    private AuthorDao authorDao;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImpl(authorDao, ioService);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        Optional<Author> actualAuthor = authorService.getById(EXISTING_AUTHOR.getId());
        assertThat(actualAuthor.get()).usingRecursiveComparison().isEqualTo(EXISTING_AUTHOR);
    }

    @DisplayName("возвращать Optional.Empty() по id несуществующего автора")
    @Test
    void shouldReturnEmptyById() {
        Optional<Author> actualAuthor = authorService.getById(NOT_EXISTING_AUTHOR_ID);
        assertThat(actualAuthor.isEmpty()).isTrue();
    }

}