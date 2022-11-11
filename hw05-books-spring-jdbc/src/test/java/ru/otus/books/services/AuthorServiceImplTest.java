package ru.otus.books.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.otus.books.dao.AuthorDao;
import ru.otus.books.domain.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("сервис для работы с авторами должен")
@SpringBootTest(classes = AuthorServiceImpl.class)
class AuthorServiceImplTest {
    private static final long NOT_EXISTING_AUTHOR_ID = 1000;
    private static final Author EXISTING_AUTHOR = new Author(3, "John Ronald Reuel Tolkien");

    @MockBean
    private IOService ioService;

    @MockBean
    @SpyBean
    private AuthorDao authorDao;

    @Autowired
    private AuthorService authorService;

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        when(authorDao.getById(EXISTING_AUTHOR.getId())).thenReturn(Optional.of(EXISTING_AUTHOR));
        Optional<Author> actualAuthor = authorService.getById(EXISTING_AUTHOR.getId());
        assertThat(actualAuthor.orElseThrow()).usingRecursiveComparison().isEqualTo(EXISTING_AUTHOR);
        verify(authorDao).getById(anyLong());
    }

    @DisplayName("возвращать Optional.Empty() при несуществующем id")
    @Test
    void shouldReturnEmpty() {
        when(authorDao.getById(NOT_EXISTING_AUTHOR_ID)).thenReturn(Optional.empty());
        var actualAuthor = authorService.getById(NOT_EXISTING_AUTHOR_ID);
        assertThat(actualAuthor).isEmpty();
        verify(authorDao).getById(anyLong());
    }

    @DisplayName("возвращать ожидаемый List авторов")
    @Test
    void shouldReturnList() {
        when(authorDao.getAll()).thenReturn(List.of(EXISTING_AUTHOR));
        var list = authorService.getAll();
        assertThat(list).containsExactlyInAnyOrder(EXISTING_AUTHOR);
        verify(authorDao).getAll();
    }

}