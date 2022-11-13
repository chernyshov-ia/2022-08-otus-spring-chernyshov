package ru.otus.books.controllers;


import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;
import ru.otus.books.services.AuthorService;
import ru.otus.books.services.BookService;
import ru.otus.books.services.GenreService;
import ru.otus.books.services.IOService;

import java.util.Optional;

import static java.util.Objects.isNull;

@ShellComponent
public class ShellController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final IOService ioService;

    public ShellController(BookService bookService, AuthorService authorService, GenreService genreService, IOService ioService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.ioService = ioService;
    }

    @ShellMethod(value = "Listing of all books", key = {"list", "l"})
    public void list() {
        for (Book book : bookService.getAll()) {
            ioService.outputString(book.toString());
        }
    }

    @ShellMethod(value = "Delete one book", key = {"delete", "del", "d"})
    public void delete(Long id) {
        bookService.deleteById(id);
        ioService.outputString( String.format("Book{id=%d} deleted%n",id));
    }

    @ShellMethod(value = "Add new book", key = {"add", "new", "append", "a"})
    public void create(String name,
                       @ShellOption(value = {"--author"}) Long authorId,
                       @ShellOption(value = {"--genre"}) Long genreId) {

        if (isNull(name)) {
            return;
        }

        var author = authorService.getById(authorId);
        if (author.isEmpty()) {
            return;
        }

        var genre = genreService.getById(genreId);
        if (genre.isEmpty()) {
            return;
        }

        Optional<Book> book = bookService.create(name, author.get(), genre.get());

        book.ifPresent(value -> ioService.outputString("ADDED: " + value));
    }

    @ShellMethod(value = "Listing of all genres", key = {"genres"})
    public void listGenres() {
        for (Genre genre : genreService.getAll()) {
            ioService.outputString(genre.toString());
        }
    }

    @ShellMethod(value = "Listing of all authors", key = {"authors"})
    public void listAuthors() {
        for (Author author : authorService.getAll()) {
            ioService.outputString(author.toString());
        }
    }


}
