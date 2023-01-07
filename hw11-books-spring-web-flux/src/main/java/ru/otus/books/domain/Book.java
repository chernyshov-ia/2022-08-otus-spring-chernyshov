package ru.otus.books.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "books")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Book {
    @Id
    private String id;

    private String name;

    private Author author;

    private Genre genre;

    public Book(String name, Author author, Genre genre) {
        this.id = null;
        this.name = name;
        this.author = author;
        this.genre = genre;
    }

}
