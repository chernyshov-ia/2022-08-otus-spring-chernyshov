package ru.otus.books.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCommentDto {
    private Long id;
    private String text;
    public static BookCommentDto fromDomainObject(ru.otus.books.domain.BookCommentDto comment) {
        return new BookCommentDto(comment.getId(), comment.getText());
    }
}
