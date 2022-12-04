package ru.otus.books.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity(name = "comments")
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
public class BookComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(updatable = false, columnDefinition = "book_id")
    private final Book book;

    public BookComment(String text, Book book) {
        this.id = 0;
        this.text = text;
        this.book = book;
    }

    public BookComment() {
        this.id = 0;
        this.text = "";
        this.book = null;
    }

    @Override
    public String toString() {
        return id + ". \'" + text + '\'';
    }
}
