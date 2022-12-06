package ru.otus.books.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookComment {
    @Id
    private String id;

    private String text;

    @DBRef
    private Book book;

    public BookComment(String text, Book book) {
        this.id = null;
        this.text = text;
        this.book = book;
    }

    @Override
    public String toString() {
        return id + ". \'" + text + '\'';
    }
}
