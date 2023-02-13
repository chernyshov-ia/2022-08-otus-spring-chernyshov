package ru.otus.books.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "genres")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Genre {
    @Id
    private String id;

    private String name;

    @Override
    public String toString() {
        return String.format("%s. %s", getId(), getName());
    }

    public Genre(String id) {
        this.id = id;
    }
}
