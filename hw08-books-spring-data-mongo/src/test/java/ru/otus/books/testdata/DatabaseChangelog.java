package ru.otus.books.testdata;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.BookComment;
import ru.otus.books.domain.Genre;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookCommentRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "chernyshov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertGenres", author = "chernyshov")
    public void insertGenres(GenreRepository repository) {
        repository.save(new Genre("1", "fantasy"));
        repository.save(new Genre("2", "novel"));
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = "chernyshov")
    public void insertAuthors(AuthorRepository repository) {
        repository.save(new Author("1", "Arthur Conan Doyle"));
        repository.save(new Author("2", "Jack London"));
        repository.save(new Author("3", "John Ronald Reuel Tolkien"));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "chernyshov")
    public void insertBooks(BookRepository bookRepo, GenreRepository genreRepo, AuthorRepository authorRepo) {
        Genre genreFantasy = genreRepo.findById("1").orElseThrow();
        Genre genreNovel = genreRepo.findById("2").orElseThrow();

        Author authorJackLondon = authorRepo.findById("2").orElseThrow();
        Author authorTolkien = authorRepo.findById("3").orElseThrow();

        bookRepo.save(new Book("1", "Hobbit", authorTolkien, genreFantasy, List.of() ));
        bookRepo.save(new Book("2", "The Lord of the Rings", authorTolkien, genreFantasy, List.of() ));
        bookRepo.save(new Book("3", "White Fang", authorJackLondon, genreNovel, List.of() ));
    }

    @ChangeSet(order = "005", id = "insertComments", author = "chernyshov")
    public void insertComments(BookRepository bookRepo, BookCommentRepository bookCommentRepo) {

        var book = bookRepo.findById("1").orElseThrow();
        book.getComments().add(bookCommentRepo.save(new BookComment("Comment text 1_1", book)));
        book.getComments().add(bookCommentRepo.save(new BookComment("Comment text 1_2", book)));
        bookRepo.save(book);

        book = bookRepo.findById("2").orElseThrow();
        book.getComments().add(bookCommentRepo.save(new BookComment("Comment text 2_1", book)));
        bookRepo.save(book);
    }

}
