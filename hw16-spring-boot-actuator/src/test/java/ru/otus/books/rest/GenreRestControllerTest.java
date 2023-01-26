package ru.otus.books.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.books.rest.dto.GenreDto;
import ru.otus.books.services.AuthorService;
import ru.otus.books.services.GenreService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreRestController.class)
class GenreRestControllerTest {
    private static final GenreDto GENRE_1 = new GenreDto(11L, "genre1");
    private static final GenreDto GENRE_2 = new GenreDto(111L, "genre2");

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Test
    void getGenres() throws Exception {
        var list = List.of(GENRE_1, GENRE_2);

        given(genreService.findAll()).willReturn(list);

        this.mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mapper.writeValueAsString(list)));
    }
}