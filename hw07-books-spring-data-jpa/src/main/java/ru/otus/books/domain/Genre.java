package ru.otus.books.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "genres")
@AllArgsConstructor
@Data
public class Genre {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final long id;

    @Column(name = "name")
    private final String name;

    public Genre() {
        this.id = 0;
        this.name = "";
    }

    @Override
    public String toString() {
        return String.format("%d. %s", getId(), getName());
    }

}
