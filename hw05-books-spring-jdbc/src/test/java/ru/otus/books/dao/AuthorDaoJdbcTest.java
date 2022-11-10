package ru.otus.books.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.books.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для работы с c авторами должно")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {
    private static final Author EXISTING_AUTHOR_1 = new Author(1, "Arthur Conan Doyle");
    private static final Author EXISTING_AUTHOR_2 = new Author(2, "Jack London");
    private static final Author EXISTING_AUTHOR_3 = new Author(3, "John Ronald Reuel Tolkien");

    @Autowired
    private AuthorDao authorDao;

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        var actualAuthor = authorDao.getById(EXISTING_AUTHOR_2.getId());
        assertThat(actualAuthor.orElseThrow()).usingRecursiveComparison().isEqualTo(EXISTING_AUTHOR_2);
    }


    @DisplayName("возвращать ожидаемый список авторов")
    @Test
    void shouldReturnExpectedBookList() {
        List<Author> actualGenreList = authorDao.getAll();
        assertThat(actualGenreList)
                .containsExactlyInAnyOrder(EXISTING_AUTHOR_1, EXISTING_AUTHOR_2, EXISTING_AUTHOR_3);
    }
}