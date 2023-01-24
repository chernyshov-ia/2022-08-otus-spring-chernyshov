package ru.otus.books.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "TMP_AUTHORS_MIGRATE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TempAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "old_id")
    private String oldId;

    private String name;
}
