package ru.otus.books.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.books.dto.BookDto;
import ru.otus.books.exceptions.NotFoundException;
import ru.otus.books.services.AuthorService;
import ru.otus.books.services.BookService;
import ru.otus.books.services.GenreService;

import javax.validation.Valid;

@Controller
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookController(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/books")
    public String listPage(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @RequestMapping("/books/{id}")
    public String viewPage(@PathVariable("id") Long id, Model model) {
        var book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "book";
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping( path = "/books/{id}/edit")
    public String editPage(@PathVariable( value = "id", required = true) Long id, Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        var book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);

        return "edit";
    }

    @GetMapping( path = "/books/new")
    public String newPage(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("book", BookDto.empty());

        return "edit";
    }


    @PostMapping("/books/save")
    public String saveBook(@Valid @ModelAttribute("book") BookDto book, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "edit";
        }

        BookDto newBook = bookService.save(book);
        return "redirect:/books/" + newBook.getId();
    }

}
