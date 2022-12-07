package ru.otus.books.repositories;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.books.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
public class AuthorRepositoryJpaTest {
    private static final Author EXISTING_AUTHOR_1 = new Author(1, "Arthur Conan Doyle");
    private static final Author EXISTING_AUTHOR_2 = new Author(2, "Jack London");
    private static final Author EXISTING_AUTHOR_3 = new Author(3, "John Ronald Reuel Tolkien");
    private static final int EXPECTED_AUTHORS_COUNT = 3;

    @Autowired
    private AuthorRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" должен загружать информацию о нужном авторе по его id")
    @Test
    void shouldFindExpectedAuthorById() {
        var optionalActualAuthor = repositoryJpa.findById(EXISTING_AUTHOR_1.getId());
        var expectedAuthor = em.find(Author.class, EXISTING_AUTHOR_1.getId());
        assertThat(optionalActualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName(" должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsListByBookId() {
        List<Author> authors = repositoryJpa.findAll();
        assertThat(authors).isNotNull().hasSize(EXPECTED_AUTHORS_COUNT)
                .allMatch(s -> !s.getName().equals(""));

        assertThat(authors).containsExactlyInAnyOrder(EXISTING_AUTHOR_1, EXISTING_AUTHOR_2, EXISTING_AUTHOR_3);
    }

}
