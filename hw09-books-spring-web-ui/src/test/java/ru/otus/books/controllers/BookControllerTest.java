package ru.otus.books.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;
import ru.otus.books.dto.BookDto;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {
    private static final Book BOOK_1 = new Book(1, "Name1", new Author(11, "Author1"), new Genre(22, "Genre1"), List.of());
    private static final String INVALID_NAME = "1";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    public void shouldReturnCorrectList() throws Exception {
        given(bookRepository.findAll()).willReturn(List.of(BOOK_1));
        this.mvc.perform(get("/books"))
                .andExpect(model().attribute("books", List.of(BOOK_1)))
                .andExpect(view().name("list"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCorrectBookAndViewById() throws Exception {
        given(bookRepository.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));
        this.mvc.perform(get("/books/{id}", BOOK_1.getId()))
                .andExpect(model().attribute("book", BOOK_1))
                .andExpect(view().name("book"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteBookById() throws Exception {
        given(bookRepository.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));

        this.mvc.perform(post("/books/{id}/delete", BOOK_1.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));

        verify(bookRepository).deleteById(BOOK_1.getId());
    }

    @Test
    void shouldReturnEditViewAndBookById() throws Exception {
        given(bookRepository.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));

        this.mvc.perform(get("/books/{id}/edit", BOOK_1.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", BookDto.fromDomainObject(BOOK_1)))
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
        given(bookRepository.save(any())).willReturn(BOOK_1);
        given(bookRepository.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));
        given(genreRepository.findById(BOOK_1.getGenre().getId())).willReturn(Optional.of(BOOK_1.getGenre()));
        given(authorRepository.findById(BOOK_1.getAuthor().getId())).willReturn(Optional.of(BOOK_1.getAuthor()));

        this.mvc.perform(post("/books/save")
                        .param("id", String.valueOf(BOOK_1.getId()))
                        .param("name", BOOK_1.getName())
                        .param("genreId", String.valueOf(BOOK_1.getGenre().getId()))
                        .param("authorId", String.valueOf(BOOK_1.getAuthor().getId()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/" + BOOK_1.getId()));

        verify(bookRepository).save(BOOK_1);
    }

    @Test
    void shouldSaveExistingBookWithErrors() throws Exception {
        var bookDto = BookDto.fromDomainObject(BOOK_1);
        bookDto.setName(INVALID_NAME);

        this.mvc.perform(post("/books/save")
                        .param("id", String.valueOf(BOOK_1.getId()))
                        .param("name", INVALID_NAME)
                        .param("genreId", String.valueOf(BOOK_1.getGenre().getId()))
                        .param("authorId", String.valueOf(BOOK_1.getAuthor().getId()))
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", bookDto))
                .andExpect(model().hasErrors())
                .andExpect(view().name("edit"));
    }

    @Test
    void shouldCreateBook() throws Exception {
        given(bookRepository.save(any())).willReturn(BOOK_1);
        given(genreRepository.findById(BOOK_1.getGenre().getId())).willReturn(Optional.of(BOOK_1.getGenre()));
        given(authorRepository.findById(BOOK_1.getAuthor().getId())).willReturn(Optional.of(BOOK_1.getAuthor()));

        this.mvc.perform(post("/books/save")
                        .param("name", BOOK_1.getName())
                        .param("genreId", String.valueOf(BOOK_1.getGenre().getId()))
                        .param("authorId", String.valueOf(BOOK_1.getAuthor().getId()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/" + BOOK_1.getId()));
    }

}
