package ru.otus.books.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.books.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для работы с жанрами должно")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {
    private static final Genre EXISTING_GENRE_1 = new Genre(1, "fantasy");
    private static final Genre EXISTING_GENRE_2 = new Genre(2, "novel");

    @Autowired
    private GenreDao genreDao;

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        Genre actualGenre = genreDao.getById(EXISTING_GENRE_2.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(EXISTING_GENRE_2);
    }


    @DisplayName("возвращать ожидаемый список жанров")
    @Test
    void shouldReturnExpectedBookList() {
        List<Genre> actualGenreList = genreDao.getAll();
        assertThat(actualGenreList)
                .containsExactlyInAnyOrder(EXISTING_GENRE_1, EXISTING_GENRE_2);
    }
}