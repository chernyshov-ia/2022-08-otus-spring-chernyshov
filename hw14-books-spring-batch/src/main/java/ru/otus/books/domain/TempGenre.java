package ru.otus.books.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TMP_GENRES_MIGRATE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TempGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "old_id")
    private String oldId;

    private String name;
}
