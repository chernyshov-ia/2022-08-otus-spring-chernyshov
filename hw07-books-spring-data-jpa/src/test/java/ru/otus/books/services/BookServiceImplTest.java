package ru.otus.books.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с книгами ")
@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {
    private static final long NOT_EXISTING_BOOK_ID = 10000;
    private static final long EXISTING_BOOK_ID = 10;
    private static final String NEW_NAME = "New name";

    private static final Book EXISTING_BOOK = new Book(EXISTING_BOOK_ID, "Hobbit",
            new Author(3, "John Ronald Reuel Tolkien" ),
            new Genre(1, "fantasy"), List.of());

    @MockBean
    private BookRepository repository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private BookService bookService;

    @DisplayName("при удалении дергается repository.deleteById() с темже id")
    @Test
    void shouldCallRepoWhenDeleteBookByIdWithSameParam() {
        when(repository.findById(EXISTING_BOOK_ID)).thenReturn(Optional.of(EXISTING_BOOK));
        bookService.deleteById(EXISTING_BOOK_ID);
        verify(repository).deleteById(EXISTING_BOOK_ID);
    }

    @DisplayName("возвращать Optional.Empty() при несуществующем id")
    @Test
    void shouldReturnEmptyWhenNotExist() {
        when(repository.findById(NOT_EXISTING_BOOK_ID)).thenReturn(Optional.empty());
        var actualBook = bookService.findById(NOT_EXISTING_BOOK_ID);
        assertThat(actualBook).isEmpty();
        verify(repository).findById(anyLong());
    }

    @DisplayName("при получение книги дергается dao.getById() с темже id")
    @Test
    void shouldCallWithSameParamDaoWhenGetBookById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(EXISTING_BOOK));
        bookService.findById(EXISTING_BOOK_ID);
        verify(repository).findById(EXISTING_BOOK_ID);
    }

    @DisplayName("при создании книги дергается repo с правильными параметрами")
    @Test
    void shouldInvokeCreateBook() {
        Book zeroBook = new Book(EXISTING_BOOK.getName(), EXISTING_BOOK.getAuthor(), EXISTING_BOOK.getGenre());

        when(authorRepository.findById(EXISTING_BOOK.getAuthor().getId())).thenReturn(Optional.of(EXISTING_BOOK.getAuthor()));
        when(genreRepository.findById(EXISTING_BOOK.getGenre().getId())).thenReturn(Optional.of(EXISTING_BOOK.getGenre()));
        when(repository.findById(anyLong())).thenReturn(Optional.of(EXISTING_BOOK));
        when(repository.save(any(Book.class))).thenReturn(EXISTING_BOOK);

        bookService.create(zeroBook.getName(), zeroBook.getAuthor().getId(), zeroBook.getGenre().getId()).orElseThrow();

        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(repository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(zeroBook);
    }

    @DisplayName("при создании книги дергается repo с правильными параметрами")
    @Test
    void shouldInvokeCreateBookOnUpdateName() {
        when(repository.findById(EXISTING_BOOK.getId())).thenReturn(Optional.of(EXISTING_BOOK));
        when(repository.save(any(Book.class))).thenReturn(EXISTING_BOOK);

        bookService.updateName(EXISTING_BOOK.getId(), NEW_NAME);

        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(repository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getName()).isEqualTo(NEW_NAME);
    }

    @DisplayName("при получении всех книг возвращать все найденные книги")
    @Test
    void shouldReturnAllBooks() {
        when(repository.findAll()).thenReturn(List.of(EXISTING_BOOK));
        List<Book> all = bookService.findAll();
        assertThat(List.of(EXISTING_BOOK)).usingRecursiveComparison().isEqualTo(all);
    }

}
