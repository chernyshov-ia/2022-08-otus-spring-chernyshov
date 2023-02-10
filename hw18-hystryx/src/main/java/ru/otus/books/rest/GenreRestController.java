package ru.otus.books.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.books.rest.dto.GenreDto;
import ru.otus.books.services.GenreService;

import java.util.List;

@RestController
public class GenreRestController {
    private final GenreService genreService;

    public GenreRestController(GenreService genreService) {
        this.genreService = genreService;
    }


    @GetMapping("/api/v1/genres")
    List<GenreDto> getGenres() {
        return genreService.findAll();
    }

}
