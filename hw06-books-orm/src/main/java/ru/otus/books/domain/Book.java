package ru.otus.books.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "books")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @BatchSize(size = 5)
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "book")
    private List<BookComment> comments;

    public Book(String name, Author author, Genre genre) {
        this.id = 0;
        this.name = name;
        this.author = author;
        this.genre = genre;
    }

    public Book(String name, Author author, Genre genre, List<BookComment> comments) {
        this.id = 0;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.comments = comments;
    }

}
