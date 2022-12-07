package ru.otus.books.dto;

import lombok.Builder;
import lombok.Data;
import ru.otus.books.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class BookDto {
    private final long id;
    private final String name;
    private final String genre;
    private final String author;
    private final List<BookCommentDto> comments;

    public static BookDto fromBookWithComments( Book book ) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor().getName())
                .genre(book.getGenre().getName())
                .comments(
                        book.getComments().stream()
                                .map(c -> new BookCommentDto(c.getId(), c.getText()))
                                .collect(Collectors.toList())
                ).build();
    }

    public static BookDto fromBookWithoutComments( Book book ) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor().getName())
                .genre(book.getGenre().getName())
                .comments(List.of())
                .build();
    }

}
