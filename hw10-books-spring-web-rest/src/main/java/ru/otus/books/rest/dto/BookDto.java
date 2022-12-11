package ru.otus.books.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.books.domain.Book;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor
public class BookDto {
    private Long id;

    @NotBlank(message = "Название книги должно быть указано")
    @Size(min = 2, max = 250)
    private String name;

    @NotNull(message = "Автор должен быть указан")
    private AuthorDto author;

    @NotNull(message = "Жанр должен быть указан")
    private GenreDto genre;

    public static BookDto empty() {
        return new BookDto();
    }

    public static BookDto fromDomainObject(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author( AuthorDto.fromDomainObject(book.getAuthor()))
                .genre( GenreDto.fromDomainObject(book.getGenre()))
                .build();
    }
}
