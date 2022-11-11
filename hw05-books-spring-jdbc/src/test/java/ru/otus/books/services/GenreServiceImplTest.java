package ru.otus.books.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;
import ru.otus.books.dao.GenreDao;
import ru.otus.books.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("сервис для работы с жанрами должен")
@SpringBootTest(classes = GenreServiceImpl.class)
class GenreServiceImplTest {
    private static final Genre EXISTING_GENRE = new Genre(2, "novel");
    private static final long NOT_EXISTING_GENRE_ID = 1000;

    @MockBean
    @SpyBean
    private GenreDao genreDao;

    @Autowired
    private GenreService genreService;

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        when(genreDao.getById(EXISTING_GENRE.getId())).thenReturn(Optional.of(EXISTING_GENRE));
        var actualGenre = genreService.getById(EXISTING_GENRE.getId());
        assertThat(actualGenre.orElseThrow()).usingRecursiveComparison().isEqualTo(EXISTING_GENRE);
        verify(genreDao).getById(EXISTING_GENRE.getId());
    }

    @DisplayName("возвращать Optional.Empty() при несуществующем id")
    @Test
    void shouldReturnEmptyById() {
        when(genreDao.getById(NOT_EXISTING_GENRE_ID)).thenReturn(Optional.empty());
        var actualGenre = genreService.getById(NOT_EXISTING_GENRE_ID);
        assertThat(actualGenre).isEmpty();
        verify(genreDao).getById(NOT_EXISTING_GENRE_ID);
    }

    @DisplayName("возвращать ожидаемый List авторов")
    @Test
    void shouldReturnList() {
        when(genreDao.getAll()).thenReturn(List.of(EXISTING_GENRE));
        var list = genreService.getAll();
        assertThat(list).containsExactlyInAnyOrder(EXISTING_GENRE);
        verify(genreDao).getAll();
    }

}