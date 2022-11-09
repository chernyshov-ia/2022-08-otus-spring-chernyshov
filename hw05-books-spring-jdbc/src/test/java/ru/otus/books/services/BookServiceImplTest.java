package ru.otus.books.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.books.dao.BookDao;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("сервис для работы с книгами должен")
@SpringBootTest
class BookServiceImplTest {
    private static final Book EXISTING_BOOK = new Book(1, "Hobbit",
            new Author(3, "John Ronald Reuel Tolkien" ),
            new Genre(1, "fantasy"));

    @MockBean
    private IOService ioService;

    @Autowired
    private BookDao bookDao;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookDao, ioService);
    }

    @DisplayName("удалять заданную книгу по её id")
    @Test
    void shouldCorrectDeleteBookById() {
        assertThatCode(() -> bookService.getById(EXISTING_BOOK.getId()))
                .doesNotThrowAnyException();

        bookDao.deleteById(EXISTING_BOOK.getId());

        assertThatThrownBy(() -> bookDao.getById(EXISTING_BOOK.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

}