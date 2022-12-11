package ru.otus.books.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
public class BookCommentDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(updatable = false, columnDefinition = "book_id")
    private final Book book;

    public BookCommentDto(String text, Book book) {
        this.id = 0;
        this.text = text;
        this.book = book;
    }

    public BookCommentDto() {
        this.id = 0;
        this.text = "";
        this.book = null;
    }

    @Override
    public String toString() {
        return id + ". \'" + text + '\'';
    }
}
