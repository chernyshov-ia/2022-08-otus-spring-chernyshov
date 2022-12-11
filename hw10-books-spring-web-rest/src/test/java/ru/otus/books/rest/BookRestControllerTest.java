package ru.otus.books.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.books.rest.dto.AuthorDto;
import ru.otus.books.rest.dto.BookDto;
import ru.otus.books.rest.dto.GenreDto;
import ru.otus.books.services.AuthorService;
import ru.otus.books.services.BookService;
import ru.otus.books.services.GenreService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(BookRestController.class)
class BookRestControllerTest {
    private static final String NEW_NAME = "NAME 2";
    private static final GenreDto GENRE_1 = new GenreDto(11L, "genre1");
    private static final GenreDto GENRE_2 = new GenreDto(111L, "genre2");
    private static final AuthorDto AUTHOR_1 = new AuthorDto(22L, "author1");
    private static final AuthorDto AUTHOR_2 = new AuthorDto(222L, "author2");
    private static final BookDto BOOK_1 = BookDto.builder()
            .id(1L)
            .name("Name1")
            .author(AUTHOR_1)
            .genre(GENRE_1)
            .build();

    private static final BookDto BOOK_2 = BookDto.builder()
            .id(2L)
            .name("Name2")
            .author(AUTHOR_1)
            .genre(GENRE_1)
            .build();

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;


    @Test
    void getBooks() throws Exception {
        given(bookService.findAll()).willReturn(List.of(BOOK_1));
        this.mvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mapper.writeValueAsString(List.of(BOOK_1))));
    }




    @Test
    void getBook() throws Exception {
        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));
        this.mvc.perform(get("/api/v1/book/{id}", BOOK_1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mapper.writeValueAsString(BOOK_1)));
    }


    @Test
    void deleteBook() throws Exception {
        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));

        this.mvc.perform(delete("/api/v1/book/{id}", BOOK_1.getId()))
                .andExpect(status().isOk());

        verify(bookService).deleteById(BOOK_1.getId());
    }

    @Test
    void putBook() throws Exception {

        var bookWithChanges = BOOK_1.toBuilder()
                .name(NEW_NAME)
                .author(AUTHOR_2)
                .genre(GENRE_2)
                .build();

        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));
        given(bookService.save(any())).willReturn(bookWithChanges);

        this.mvc.perform(
                put("/api/v1/book/{id}", BOOK_1.getId())
                        .content(mapper.writeValueAsString(bookWithChanges))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mapper.writeValueAsString(bookWithChanges)));

        verify(bookService).save(bookWithChanges);
    }

    @Test
    void postBook() throws Exception {
        var bookNew = BOOK_2.toBuilder().id(null).build();

        given(bookService.save(any())).willReturn(BOOK_2);

        this.mvc.perform(
                        post("/api/v1/books")
                                .content(mapper.writeValueAsString(bookNew))
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mapper.writeValueAsString(BOOK_2)));

        verify(bookService).save(bookNew);
    }

    @Test
    void getGenres() throws Exception {
        var list = List.of(GENRE_1, GENRE_2);

        given(genreService.findAll()).willReturn(list);

        this.mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mapper.writeValueAsString(list)));
    }

    @Test
    void getAuthors() throws Exception {
        var list = List.of(AUTHOR_1, AUTHOR_2);

        given(authorService.findAll()).willReturn(list);

        this.mvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mapper.writeValueAsString(list)));
    }
}
