package ru.otus.books.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.otus.books.dao.BookDao;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("сервис для работы с книгами должен")
@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {
    private static final long NOT_EXISTING_BOOK_ID = 10000;

    private static final Book EXISTING_BOOK = new Book(1, "Hobbit",
            new Author(3, "John Ronald Reuel Tolkien" ),
            new Genre(1, "fantasy"));

    @MockBean
    @SpyBean
    private BookDao bookDao;

    @Autowired
    BookService bookService;

    @DisplayName("при удалении дергается dao.deleteById() с темже id")
    @Test
    void shouldCallDaoWhenDeleteBookByIdWithSameParam() {
        when(bookDao.getById(EXISTING_BOOK.getId())).thenReturn(Optional.of(EXISTING_BOOK));
        bookService.deleteById(EXISTING_BOOK.getId());
        verify(bookDao).deleteById(EXISTING_BOOK.getId());
    }

    @DisplayName("возвращать Optional.Empty() при несуществующем id")
    @Test
    void shouldReturnEmptyWhenNotExist() {
        when(bookDao.getById(anyLong())).thenReturn(Optional.empty());
        var actualAuthor = bookService.getById(NOT_EXISTING_BOOK_ID);
        assertThat(actualAuthor).isEmpty();
        verify(bookDao).getById(anyLong());
    }

    @DisplayName("при получение книги дергается dao.getById() с темже id")
    @Test
    void shouldCallWithSameParamDaoWhenGetBookById() {
        when(bookDao.getById(anyLong())).thenReturn(Optional.of(EXISTING_BOOK));
        bookService.getById(EXISTING_BOOK.getId());
        verify(bookDao).getById(EXISTING_BOOK.getId());
    }

    @DisplayName("при получение книги возвращается корректная книга")
    @Test
    void shouldReturnCorrectBookWhenGetBookById() {
        when(bookDao.getById(anyLong())).thenReturn(Optional.of(EXISTING_BOOK));
        var book = bookService.getById(EXISTING_BOOK.getId());
        assertThat(book.orElseThrow()).usingRecursiveComparison().isEqualTo(EXISTING_BOOK);
    }

    @DisplayName("при создании книги дергается dao с правильными параметрами")
    @Test
    void shouldInvokeCreateBook() {
        Book zeroBook = new Book(EXISTING_BOOK.getName(), EXISTING_BOOK.getAuthor(), EXISTING_BOOK.getGenre());
        when(bookDao.getById(anyLong())).thenReturn(Optional.of(EXISTING_BOOK));
        when(bookDao.insert(zeroBook)).thenReturn(EXISTING_BOOK.getId());
        bookService.create(zeroBook.getName(), zeroBook.getAuthor(), zeroBook.getGenre());
        verify(bookDao).insert(zeroBook);
    }

}