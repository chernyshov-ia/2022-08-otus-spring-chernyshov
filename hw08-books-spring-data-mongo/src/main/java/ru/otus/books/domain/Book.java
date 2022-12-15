package ru.otus.books.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.books.dto.BookCommentDto;

import java.util.List;


@Document(collection = "books")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    private String id;

    private String name;

    private Author author;

    private Genre genre;

    private List<BookComment> comments;

    public Book(String name, Author author, Genre genre) {
        this.id = null;
        this.name = name;
        this.author = author;
        this.genre = genre;
    }

    public Book(String name, Author author, Genre genre, List<BookComment> comments) {
        this.id = null;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.comments = comments;
    }

}
