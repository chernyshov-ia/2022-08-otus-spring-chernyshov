package ru.otus.books.mongock.changelog;

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
        repository.save(new Genre("G1", "fantasy")).block();
        repository.save(new Genre("G2", "novel")).block();
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = "chernyshov")
    public void insertAuthors(AuthorRepository repository) {
        repository.save(new Author("A1", "Arthur Conan Doyle")).block();
        repository.save(new Author("A2", "Jack London")).block();
        repository.save(new Author("A3", "John Ronald Reuel Tolkien")).block();
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "chernyshov")
    public void insertBooks(BookRepository bookRepo, GenreRepository genreRepo, AuthorRepository authorRepo) {
        Genre genreFantasy = genreRepo.findById("G1").block();
        Genre genreNovel = genreRepo.findById("G2").block();

        Author authorJackLondon = authorRepo.findById("A2").block();
        Author authorTolkien = authorRepo.findById("A3").block();

        bookRepo.save(new Book(null, "Hobbit", authorTolkien, genreFantasy)).block();
        bookRepo.save(new Book(null, "The Lord of the Rings", authorTolkien, genreFantasy )).block();
        bookRepo.save(new Book(null, "White Fang", authorJackLondon, genreNovel)).block();
    }


}
