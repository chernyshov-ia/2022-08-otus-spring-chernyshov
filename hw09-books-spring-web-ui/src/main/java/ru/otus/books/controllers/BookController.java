package ru.otus.books.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.BookComment;
import ru.otus.books.dto.BookCommentDto;
import ru.otus.books.dto.BookDto;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping("")
    public String listPage(Model model) {
        var books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @RequestMapping("/view")
    public String viewPage(@RequestParam("id") Long id, Model model) {
        var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        var comments = book.getComments().stream()
                .map(BookCommentDto::fromDomainObject)
                .collect(Collectors.toList());
        model.addAttribute("comments", comments);
        return "book";
    }

    @PostMapping("/delete")
    public String deleteBook(@RequestParam("id") Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam( value = "id", required = false) Long id, Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        if (id == null) {
            model.addAttribute("book", BookDto.empty());
        } else {
            var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
            model.addAttribute("book", BookDto.fromDomainObject(book));
        }
        return "edit";
    }

    @PostMapping("/edit")
    public String editBook(@Valid @ModelAttribute("book") BookDto book, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorRepository.findAll());
            model.addAttribute("genres", genreRepository.findAll());
            return "edit";
        }

        var genre = genreRepository.findById(book.getGenreId()).orElseThrow(NotFoundException::new);
        var author = authorRepository.findById(book.getAuthorId()).orElseThrow(NotFoundException::new);

        Book bookDomain;

        if (book.getId() == null) {
            bookDomain = new Book(book.getName(), author, genre);
        } else {
            bookDomain = bookRepository.findById(book.getId()).orElseThrow(NotFoundException::new);
            bookDomain.setName(book.getName());
            bookDomain.setAuthor(author);
            bookDomain.setGenre(genre);
        }
        bookRepository.save(bookDomain);

        return "redirect:/books/view?id=" + bookDomain.getId();
    }

}
