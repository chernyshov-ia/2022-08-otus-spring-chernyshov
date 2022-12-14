package ru.otus.books.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Genre;
import ru.otus.books.dto.BookDto;
import ru.otus.books.services.AuthorService;
import ru.otus.books.services.BookService;
import ru.otus.books.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {
    private static final Genre GENRE_1 = new Genre(11L, "genre1");
    private static final Author AUTHOR_1 = new Author(22L, "author1");
    private static final BookDto BOOK_1 = BookDto.builder()
            .id(1L)
            .name("Name1")
            .authorId(AUTHOR_1.getId())
            .authorName(AUTHOR_1.getName())
            .genreId(GENRE_1.getId())
            .genreName(GENRE_1.getName())
            .comments(List.of())
            .build();

    private static final String INVALID_NAME = "1";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    @Test
    void shouldReturnCorrectList() throws Exception {
        given(bookService.findAll()).willReturn(List.of(BOOK_1));
        this.mvc.perform(get("/books"))
                .andExpect(model().attribute("books", List.of(BOOK_1)))
                .andExpect(view().name("list"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCorrectBookAndViewById() throws Exception {
        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));
        this.mvc.perform(get("/books/{id}", BOOK_1.getId()))
                .andExpect(model().attribute("book", BOOK_1))
                .andExpect(view().name("book"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteBookById() throws Exception {
        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));

        this.mvc.perform(post("/books/{id}/delete", BOOK_1.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));

        verify(bookService).deleteById(BOOK_1.getId());
    }

    @Test
    void shouldReturnEditViewAndBookById() throws Exception {
        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));

        this.mvc.perform(get("/books/{id}/edit", BOOK_1.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", BOOK_1))
                .andExpect(view().name("edit"));
    }

    @Test
    void shouldReturnEditViewWhenCreatingBook() throws Exception {
        this.mvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"));
    }

    @Test
    void shouldSaveExistingBook() throws Exception {
        given(bookService.save(any())).willReturn(BOOK_1);
        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));
        given(genreService.findById(BOOK_1.getGenreId())).willReturn(Optional.of(GENRE_1));
        given(authorService.findById(BOOK_1.getAuthorId())).willReturn(Optional.of(AUTHOR_1));

        this.mvc.perform(post("/books/save")
                        .param("id", String.valueOf(BOOK_1.getId()))
                        .param("name", BOOK_1.getName())
                        .param("genreId", String.valueOf(BOOK_1.getGenreId()))
                        .param("authorId", String.valueOf(BOOK_1.getAuthorId()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/" + BOOK_1.getId()));

        verify(bookService).save(
                BOOK_1.toBuilder()
                        .authorName(null)
                        .genreName(null)
                        .comments(null)
                        .build()
        );
    }

    @Test
    void shouldSaveExistingBookWithErrors() throws Exception {
        var bookDto = BookDto.builder()
                .id(BOOK_1.getId())
                .name(INVALID_NAME)
                .authorId(BOOK_1.getAuthorId())
                .genreId(BOOK_1.getGenreId())
                .build();

        this.mvc.perform(post("/books/save")
                        .param("id", String.valueOf(BOOK_1.getId()))
                        .param("name", INVALID_NAME)
                        .param("genreId", String.valueOf(BOOK_1.getGenreId()))
                        .param("authorId", String.valueOf(BOOK_1.getAuthorId()))
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", bookDto))
                .andExpect(model().hasErrors())
                .andExpect(view().name("edit"));
    }

    @Test
    void shouldCreateBook() throws Exception {
        given(bookService.save(any())).willReturn(BOOK_1);
        given(genreService.findById(BOOK_1.getGenreId())).willReturn(Optional.of(GENRE_1));
        given(authorService.findById(BOOK_1.getAuthorId())).willReturn(Optional.of(AUTHOR_1));

        this.mvc.perform(post("/books/save")
                        .param("name", BOOK_1.getName())
                        .param("genreId", String.valueOf(BOOK_1.getGenreId()))
                        .param("authorId", String.valueOf(BOOK_1.getAuthorId()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/" + BOOK_1.getId()));
    }

}
