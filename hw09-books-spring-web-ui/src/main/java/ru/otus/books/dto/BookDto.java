package ru.otus.books.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.books.domain.Book;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BookDto {
    private Long id;

    @NotBlank(message = "Название книги должно быть указано")
    @Size(min = 2, max = 250)
    private String name;

    @NotNull(message = "Автор должен быть указан")
    private Long authorId;

    @NotNull(message = "Жанр должен быть указан")
    private Long genreId;

    private List<BookCommentDto> comments;

    public static BookDto empty() {
        return new BookDto();
    }

    public static BookDto from(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .authorId(book.getAuthor().getId())
                .genreId(book.getGenre().getId())
                .build();
    }

    public static BookDto fromWithComments(Book book) {
        var commentsDto = book.getComments().stream()
                .map(BookCommentDto::fromDomainObject)
                .collect(Collectors.toList());

        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .authorId(book.getAuthor().getId())
                .genreId(book.getGenre().getId())
                .comments(commentsDto)
                .build();
    }

}
