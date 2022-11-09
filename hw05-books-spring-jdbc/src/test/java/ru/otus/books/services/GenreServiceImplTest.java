package ru.otus.books.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.books.dao.GenreDao;
import ru.otus.books.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("сервис для работы с жанрами должен")
@SpringBootTest
class GenreServiceImplTest {
    private static final Genre EXISTING_GENRE = new Genre(2, "novel");
    private static final long NOT_EXISTING_GENRE_ID = 1000;

    @MockBean
    private IOService ioService;

    @Autowired
    private GenreDao genreDao;

    private GenreService genreService;

    @BeforeEach
    void setUp() {
        genreService = new GenreServiceImpl(genreDao, ioService);
    }

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        Optional<Genre> actualGenre = genreService.getById(EXISTING_GENRE.getId());
        assertThat(actualGenre.get()).usingRecursiveComparison().isEqualTo(EXISTING_GENRE);
    }

    @DisplayName("возвращать Optional.Empty() по id несуществующего жанра")
    @Test
    void shouldReturnEmptyById() {
        Optional<Genre> actualGenre = genreService.getById(NOT_EXISTING_GENRE_ID);
        assertThat(actualGenre.isEmpty()).isTrue();
    }

}