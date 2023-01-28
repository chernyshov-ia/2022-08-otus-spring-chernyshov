package ru.otus.books.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "TMP_BOOKS_MIGRATE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TempBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "old_id")
    private String oldId;

    private String name;

    @Column(name = "genre_id")
    private String genreId;

    @Column(name = "author_id")
    private String authorId;
}
