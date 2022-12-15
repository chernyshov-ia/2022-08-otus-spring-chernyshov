package ru.otus.books.dto;

import lombok.Data;
import ru.otus.books.domain.BookComment;

@Data
public class BookCommentDto {
    private final String id;
    private final String text;

    public static BookCommentDto from(BookComment comment) {
        return new BookCommentDto(comment.getId(), comment.getText());
    }
}
