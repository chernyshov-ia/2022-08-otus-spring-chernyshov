package ru.otus.books.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authors")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Author {

    @Id
    private String id;

    private String name;

    @Override
    public String toString() {
        return String.format("%s. %s", getId(), getName());
    }

    public Author(String id) {
        this.id = id;
    }
}
