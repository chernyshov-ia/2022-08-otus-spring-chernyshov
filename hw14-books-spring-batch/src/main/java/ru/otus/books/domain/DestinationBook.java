package ru.otus.books.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOOKS")
@NoArgsConstructor
@Getter
@Setter
public class DestinationBook {
    @Id
    private Integer id;

    private String name;

    @Column(name = "genre_id")
    private Integer genreId;

    @Column(name = "author_id")
    private Integer authorId;

    public DestinationBook(Integer id, String name, Integer genreId, Integer authorId) {
        this.id = id;
        this.name = name;
        this.genreId = genreId;
        this.authorId = authorId;
    }
}
