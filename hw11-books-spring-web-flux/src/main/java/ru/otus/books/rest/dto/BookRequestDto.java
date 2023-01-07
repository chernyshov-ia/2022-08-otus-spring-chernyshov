package ru.otus.books.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor
public class BookRequestDto {
    @NotBlank(message = "Название книги должно быть указано")
    @Size(min = 2, max = 250)
    private String name;

    @NotNull(message = "Автор должен быть указан")
    private String authorId;

    @NotNull(message = "Жанр должен быть указан")
    private String genreId;
}
