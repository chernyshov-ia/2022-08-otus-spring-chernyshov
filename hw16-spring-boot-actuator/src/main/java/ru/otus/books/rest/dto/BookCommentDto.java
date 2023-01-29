package ru.otus.books.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.books.domain.BookComment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCommentDto {
    private Long id;
    private String text;
    public static BookCommentDto fromDomainObject(BookComment comment) {
        return new BookCommentDto(comment.getId(), comment.getText());
    }
}
