package ru.otus.books.testdata;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;

@ChangeLog
public class DatabaseChangelog {


    @ChangeSet(order = "001", id = "dropDb", author = "chernyshov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertGenres", author = "chernyshov")
    public void insertGenres(GenreRepository repository) {
        repository.save(new Genre("G1", "Genre 1"));
        repository.save(new Genre("G2", "Genre 2"));
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = "chernyshov")
    public void insertAuthors(AuthorRepository repository) {
        repository.save(new Author("A1", "Author 1"));
        repository.save(new Author("A2", "Author 2"));
        repository.save(new Author("A3", "Author 3"));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "chernyshov")
    public void insertBooks(BookRepository bookRepo, GenreRepository genreRepo, AuthorRepository authorRepo) {
        Genre g1 = genreRepo.findById("G1").orElseThrow();
        Genre g2 = genreRepo.findById("G2").orElseThrow();

        Author a2 = authorRepo.findById("A2").orElseThrow();
        Author a3 = authorRepo.findById("A3").orElseThrow();

        bookRepo.save(new Book("B1", "Book 1", a3, g1 ));
        bookRepo.save(new Book("B2", "Book 2", a3, g1 ));
        bookRepo.save(new Book("B3", "Book 3", a2, g2 ));
    }

}
