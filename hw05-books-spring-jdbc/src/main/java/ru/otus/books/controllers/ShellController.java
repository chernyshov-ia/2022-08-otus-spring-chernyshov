package ru.otus.books.controllers;


import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.books.services.AuthorService;
import ru.otus.books.services.BookService;
import ru.otus.books.services.GenreService;
import ru.otus.books.services.IOService;

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
        bookService.list();
    }

    @ShellMethod(value = "Delete one book", key = {"delete", "del", "d"})
    public void delete(@ShellOption(value = {}, help = "№ for deleting") Long id) {
        bookService.deleteById(id);
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

        bookService.create(name, author.get(), genre.get());
    }

    @ShellMethod(value = "Listing of all genres", key = {"genres"})
    public void listGenres() {
        genreService.list();
    }

    @ShellMethod(value = "Listing of all authors", key = {"authors"})
    public void listAuthors() {
        authorService.list();
    }


}
