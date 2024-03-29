package ru.otus.books.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.books.domain.Author;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    @NotNull(message = "Автор должен быть указан")
    private String id;
    private String name;

    public AuthorDto(String id) {
        this.id = id;
    }

    public static AuthorDto fromDomainObject(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }
}
