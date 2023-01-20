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
public class SourceChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "chernyshov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertGenres", author = "chernyshov")
    public void insertGenres(GenreRepository repository) {
        repository.save(new Genre("G1", "fantasy"));
        repository.save(new Genre("G2", "novel"));
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = "chernyshov")
    public void insertAuthors(AuthorRepository repository) {
        repository.save(new Author("A1", "Arthur Conan Doyle"));
        repository.save(new Author("A2", "Jack London"));
        repository.save(new Author("A3", "John Ronald Reuel Tolkien"));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "chernyshov")
    public void insertBooks(BookRepository bookRepo, GenreRepository genreRepo, AuthorRepository authorRepo) {
        Genre genreFantasy = genreRepo.findById("G1").orElseThrow();
        Genre genreNovel = genreRepo.findById("G2").orElseThrow();

        Author authorJackLondon = authorRepo.findById("A2").orElseThrow();
        Author authorTolkien = authorRepo.findById("A3").orElseThrow();

        bookRepo.save(new Book("B1", "Hobbit", authorTolkien, genreFantasy ));
        bookRepo.save(new Book("B2", "The Lord of the Rings", authorTolkien, genreFantasy ));
        bookRepo.save(new Book("B3", "White Fang", authorJackLondon, genreNovel ));
        bookRepo.save(new Book("B4", "White Fang(2)", authorJackLondon, genreNovel ));
        bookRepo.save(new Book("B5", "White Fang(3)", authorJackLondon, genreNovel ));
        bookRepo.save(new Book("B6", "White Fang(4)", authorJackLondon, genreNovel ));
        bookRepo.save(new Book("B7", "White Fang(5)", authorJackLondon, genreNovel ));
        bookRepo.save(new Book("B8", "White Fang(6)", authorJackLondon, genreNovel ));
    }


}
