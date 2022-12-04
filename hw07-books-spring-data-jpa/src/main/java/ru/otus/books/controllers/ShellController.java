package ru.otus.books.controllers;


import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.BookComment;
import ru.otus.books.domain.Genre;
import ru.otus.books.dto.BookDto;
import ru.otus.books.services.AuthorService;
import ru.otus.books.services.BookCommentService;
import ru.otus.books.services.BookService;
import ru.otus.books.services.GenreService;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class ShellController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookCommentService commentService;

    public ShellController(BookService bookService, AuthorService authorService, GenreService genreService, BookCommentService commentService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.commentService = commentService;
    }

    @ShellMethod(value = "Listing of all books", key = {"list", "l"})
    public List<BookDto> list() {
        return bookService.findAll();
    }


    @ShellMethod(value = "Delete one book", key = {"delete", "del", "d"})
    public String delete(Long id) {
        bookService.deleteById(id);
        return String.format("Book{id=%d} deleted%n", id);
    }

    @ShellMethod(value = "Show one book", key = {"book"})
    public String book(Long id) {
        Optional<BookDto> book = bookService.findById(id);
        return book.isPresent() ? book.get().toString() : "not found";
    }

    @ShellMethod(value = "Add new book", key = {"add", "new", "append", "a"})
    public String create(String name,
                         @ShellOption(value = {"--author"}) Long authorId,
                         @ShellOption(value = {"--genre"}) Long genreId) {

        Optional<BookDto> book = bookService.create(name, authorId, genreId);

        return book.isPresent() ? book.get().toString() : "not created";
    }

    @ShellMethod(value = "Update book", key = {"update", "upd"})
    public String update(Long id, String name) {
        Optional<BookDto> book = bookService.updateName(id, name);
        return book.isPresent() ? book.get().toString() : "not updated";
    }

    @ShellMethod(value = "Listing of all genres", key = {"genres"})
    public List<Genre> listGenres() {
        return genreService.findAll();
    }

    @ShellMethod(value = "Listing of all authors", key = {"authors"})
    public List<Author> listAuthors() {
        return authorService.findAll();
    }

    @ShellMethod(value = "Listing comment of the book", key = {"comments"})
    public List<BookComment> listComments(Long id) {
        return commentService.findByBookId(id);
    }

    @ShellMethod(value = "Show one comment", key = {"comment"})
    public String comment(Long id) {
        Optional<BookComment> optionalBookComment = commentService.findById(id);
        return optionalBookComment.isPresent() ? optionalBookComment.get().toString() : "not found";
    }

    @ShellMethod(value = "Delete one comment", key = {"deletecomment", "delcomment", "delete_comment", "del_comment"})
    public String deleteComment(Long id) {
        commentService.deleteById(id);
        return String.format("Comment{id=%d} deleted%n", id);
    }

    @ShellMethod(value = "Update one comment", key = {"updatecomment", "updcomment", "update_comment", "upd_comment"})
    public String updateComment(Long id, String text) {
        Optional<BookComment> comment = commentService.updateTextById(id, text);
        return comment.isPresent() ? comment.get().toString() : "not updated";
    }

    @ShellMethod(value = "New comment", key = {"newcomment", "new_comment"})
    public String newComment(Long bookId, String text) {
        Optional<BookComment> comment = commentService.create(text, bookId);
        return comment.isPresent() ? comment.get().toString() : "not created";
    }

}
