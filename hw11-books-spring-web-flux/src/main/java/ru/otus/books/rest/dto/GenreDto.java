package ru.otus.books.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.books.domain.Genre;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    @NotNull(message = "Жанр должен быть указан")
    private String id;
    private String name;

    public GenreDto(String id) {
        this.id = id;
    }

    public static GenreDto fromDomainObject(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
