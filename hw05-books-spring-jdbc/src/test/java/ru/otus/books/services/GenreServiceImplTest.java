package ru.otus.books.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.books.dao.GenreDao;
import ru.otus.books.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@DisplayName("сервис для работы с жанрами должен")
@SpringBootTest
class GenreServiceImplTest {
    private static final Genre EXISTING_GENRE = new Genre(2, "novel");
    private static final long NOT_EXISTING_GENRE_ID = 1000;

    @MockBean
    private IOService ioService;

    @MockBean
    private GenreDao genreDao;

    private GenreService genreService;

    @BeforeEach
    void setUp() {
        genreService = new GenreServiceImpl(genreDao, ioService);
    }

    @Configuration
    public static class NestedConfiguration {
    }

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        when(genreDao.getById(EXISTING_GENRE.getId())).thenReturn(EXISTING_GENRE);
        Optional<Genre> actualGenre = genreService.getById(EXISTING_GENRE.getId());
        assertThat(actualGenre.get()).usingRecursiveComparison().isEqualTo(EXISTING_GENRE);
    }

    @DisplayName("возвращать Optional.Empty() по id при возникновении EmptyResultDataAccessException")
    @Test
    void shouldReturnEmptyById() {
        when(genreDao.getById(NOT_EXISTING_GENRE_ID)).thenThrow(EmptyResultDataAccessException.class);
        Optional<Genre> actualGenre = genreService.getById(NOT_EXISTING_GENRE_ID);
        assertThat(actualGenre.isEmpty()).isTrue();
    }

    @DisplayName("выбрасывает исключение, если исключение не EmptyResultDataAccessException")
    @Test
    void shouldRethrowUnexpectedException() {
        doThrow(RuntimeException.class).when(genreDao).getById(anyLong());
        assertThatCode(() -> {
            Optional<Genre> actualGenre = genreService.getById(NOT_EXISTING_GENRE_ID);
        }).hasNoSuppressedExceptions();
    }

}