package ru.otus.books.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "authors")
@AllArgsConstructor
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final long id;

    @Column(name = "name")
    private final String name;

    @Override
    public String toString() {
        return String.format("%d. %s", getId(), getName());
    }

    public Author() {
        this.id = 0;
        this.name = "";
    }
}
