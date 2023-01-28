package ru.otus.books.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.books.rest.dto.AuthorDto;
import ru.otus.books.rest.dto.BookDto;
import ru.otus.books.rest.dto.BookRequestDto;
import ru.otus.books.rest.dto.GenreDto;
import ru.otus.books.services.AuthorService;
import ru.otus.books.services.BookService;
import ru.otus.books.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



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
        this.mvc.perform(get("/api/v1/books/{id}", BOOK_1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mapper.writeValueAsString(BOOK_1)));
    }


    @Test
    void deleteBook() throws Exception {
        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));

        this.mvc.perform(delete("/api/v1/books/{id}", BOOK_1.getId()))
                .andExpect(status().isOk());

        verify(bookService).deleteById(BOOK_1.getId());
    }

    @Test
    void putBook() throws Exception {

        var bookWithChanges = BOOK_1.toBuilder()
                .name(NEW_NAME)
                .author(new AuthorDto(AUTHOR_2.getId()))
                .genre(new GenreDto(GENRE_2.getId()))
                .build();

        var payload = BookRequestDto.builder()
                .name(bookWithChanges.getName())
                .authorId(bookWithChanges.getAuthor().getId())
                .genreId(bookWithChanges.getGenre().getId())
                .build();

        given(bookService.findById(BOOK_1.getId())).willReturn(Optional.of(BOOK_1));
        given(bookService.save(any())).willReturn(bookWithChanges);



        this.mvc.perform(
                put("/api/v1/books/{id}", BOOK_1.getId())
                        .content(mapper.writeValueAsString(payload))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mapper.writeValueAsString(bookWithChanges)));

        verify(bookService).save(bookWithChanges);
    }

    @Test
    void postBook() throws Exception {
        var payload = BookRequestDto.builder()
                .name(BOOK_2.getName())
                .authorId(BOOK_2.getAuthor().getId())
                .genreId(BOOK_2.getGenre().getId())
                .build();


        var bookNew = BookDto.builder()
                .name(payload.getName())
                .author( new AuthorDto(payload.getAuthorId()))
                .genre(new GenreDto(payload.getGenreId()))
                .build();


        given(bookService.save(any())).willReturn(BOOK_2);

        this.mvc.perform(
                        post("/api/v1/books")
                                .content(mapper.writeValueAsString(payload))
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mapper.writeValueAsString(BOOK_2)));

        verify(bookService).save(bookNew);
    }
}
