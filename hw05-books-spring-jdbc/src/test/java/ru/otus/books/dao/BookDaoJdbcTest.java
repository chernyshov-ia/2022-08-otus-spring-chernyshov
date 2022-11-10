package ru.otus.books.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для работы с книгами должно")
@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {

    private static final int EXPECTED_BOOKS_COUNT = 1;
    private static final Book EXISTING_BOOK = new Book(1, "Hobbit",
            new Author(3, "John Ronald Reuel Tolkien" ),
            new Genre(1, "fantasy"));

    @Autowired
    private BookDaoJdbc bookDao;

    @DisplayName("возвращать ожидаемое количество книг в БД")
    @Test
    void shouldReturnExpectedBookCount() {
        long actualBooksCount = bookDao.count();
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    void shouldInsertBook() {
        Book expectedBook = new Book(2, "The Lord of the Rings",
                new Author(3, "John Ronald Reuel Tolkien" ),
                new Genre(1, "fantasy"));
        bookDao.insert(expectedBook);
        Book actualBook = bookDao.getById(expectedBook.getId()).orElseThrow();
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectedBookById() {
        var actualBook = bookDao.getById(EXISTING_BOOK.getId());
        assertThat(actualBook.orElseThrow()).usingRecursiveComparison().isEqualTo(EXISTING_BOOK);
    }

    @DisplayName("удалять заданную книгу по её id")
    @Test
    void shouldCorrectDeleteBookById() {
        var book = bookDao.getById(EXISTING_BOOK.getId());
        assertThat(book.isPresent()).isTrue();

        bookDao.deleteById(EXISTING_BOOK.getId());

        book = bookDao.getById(EXISTING_BOOK.getId());
        assertThat(book.isEmpty()).isTrue();
    }

    @DisplayName("возвращать ожидаемый список книг")
    @Test
    void shouldReturnExpectedBookList() {
        List<Book> actualBookList = bookDao.getAll();
        assertThat(actualBookList)
                .containsExactlyInAnyOrder(EXISTING_BOOK);
    }
}