package ru.otus.books.repositories;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами ")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
public class GenreRepositoryJpaTest {

    private static final Genre EXISTING_GENRE_1 = new Genre(1, "fantasy");
    private static final Genre EXISTING_GENRE_2 = new Genre(2, "novel");
    private static final int EXPECTED_GENRES_COUNT = 2;


    @Autowired
    private GenreRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" должен загружать информацию о нужном авторе по его id")
    @Test
    void shouldFindExpectedGenreById() {
        var optionalActualGenre = repositoryJpa.findById(EXISTING_GENRE_1.getId());
        var expectedGenre = em.find(Genre.class, EXISTING_GENRE_1.getId());
        assertThat(optionalActualGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName(" должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectGenresListByBookId() {
        List<Genre> genres = repositoryJpa.findAll();
        assertThat(genres).isNotNull().hasSize(EXPECTED_GENRES_COUNT)
                .allMatch(s -> !s.getName().equals(""));

        assertThat(genres).containsExactlyInAnyOrder(EXISTING_GENRE_1, EXISTING_GENRE_2);
    }
}
