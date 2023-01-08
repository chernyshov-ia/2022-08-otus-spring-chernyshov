package ru.otus.books.controllers;


import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.AtMost;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.books.config.SecurityConfig;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Genre;
import ru.otus.books.domain.User;
import ru.otus.books.dto.BookDto;
import ru.otus.books.services.AuthorService;
import ru.otus.books.services.BookService;
import ru.otus.books.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
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

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(authorities = {"ANOTHER_ROLE"})
    void shouldNotReturnCorrectListWithoutRoleUser() throws Exception {
        this.mvc.perform(get("/books"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldReturnCorrectList() throws Exception {
        given(bookService.findAll()).willReturn(List.of(BOOK_1));
        this.mvc.perform(get("/books"))
                .andExpect(model().attribute("books", List.of(BOOK_1)))
                .andExpect(view().name("list"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUnauthenticatedWhenGetBooks() throws Exception {
        this.mvc.perform(get("/books")).andExpect(unauthenticated());
    }



    @Test
    @WithMockUser(authorities = {"ANOTHER_ROLE"})
    void shouldNotReturnCorrectBookAndViewByIdWithoutRoleUser() throws Exception {
        this.mvc.perform(get("/books/{id}", BOOK_1.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldReturnCorrectBookAndViewById() throws Exception {
        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));
        this.mvc.perform(get("/books/{id}", BOOK_1.getId()))
                .andExpect(model().attribute("book", BOOK_1))
                .andExpect(view().name("book"))
                .andExpect(status().isOk());
    }


    @Test
    void shouldUnauthenticatedWhenGetBook() throws Exception {
        this.mvc.perform(get("/books/{id}", BOOK_1.getId())).andExpect(unauthenticated());
    }


    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldNotDeleteBookByIdWithoutRoleAdmin() throws Exception {
        this.mvc.perform(post("/books/{id}/delete", BOOK_1.getId()))
                .andExpect(status().is4xxClientError());

        verify(bookService, new AtMost(0)).deleteById(BOOK_1.getId());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldDeleteBookByIdByRoleAdmin() throws Exception {
        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));

        this.mvc.perform(post("/books/{id}/delete", BOOK_1.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));

        verify(bookService).deleteById(BOOK_1.getId());
    }

    @Test
    void shouldUnauthenticatedWhenDeleteBook() throws Exception {
        this.mvc.perform(post("/books/{id}/delete", BOOK_1.getId())).andExpect(unauthenticated());
    }


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldReturnEditViewAndBookByIdWithAdminRole() throws Exception {
        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));

        this.mvc.perform(get("/books/{id}/edit", BOOK_1.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", BOOK_1))
                .andExpect(view().name("edit"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldNotReturnEditViewAndBookByIdWithoutAdminRole() throws Exception {
        this.mvc.perform(get("/books/{id}/edit", BOOK_1.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldUnauthenticatedWhenEditBookPage() throws Exception {
        this.mvc.perform(get("/books/{id}/edit", BOOK_1.getId())).andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldReturnEditViewWhenCreatingBookWithRoleAdmin() throws Exception {
        this.mvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void shouldNotReturnEditViewWhenCreatingBookWithoutRoleAdmin() throws Exception {
        this.mvc.perform(get("/books/new"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldUnauthenticatedWhenNewBookPage() throws Exception {
        this.mvc.perform(get("/books/new")).andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void shouldSaveExistingBookWithRoleAdmin() throws Exception {
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
    @WithMockUser
    void shouldNotSaveExistingBookWithoutRoleAdmin() throws Exception {
        this.mvc.perform(post("/books/save")
                        .param("id", String.valueOf(BOOK_1.getId()))
                        .param("name", BOOK_1.getName())
                        .param("genreId", String.valueOf(BOOK_1.getGenreId()))
                        .param("authorId", String.valueOf(BOOK_1.getAuthorId()))
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldUnauthenticatedWhenSaveBook() throws Exception {
        this.mvc.perform(post("/books/save")).andExpect(unauthenticated());
    }


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(authorities = {"ROLE_ADMIN"})
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

    @Test
    @WithMockUser
    void shouldNotCreateBookWithoutRoleAdmin() throws Exception {
        this.mvc.perform(post("/books/save")
                        .param("name", BOOK_1.getName())
                        .param("genreId", String.valueOf(BOOK_1.getGenreId()))
                        .param("authorId", String.valueOf(BOOK_1.getAuthorId()))
                )
                .andExpect(status().is4xxClientError());
    }

}
