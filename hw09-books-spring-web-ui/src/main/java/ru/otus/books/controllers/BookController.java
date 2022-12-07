package ru.otus.books.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.books.repositories.AuthorRepository;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.repositories.GenreRepository;

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
    public String getBooks(Model model) {
        var books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @RequestMapping("/view")
    public String getBook(@RequestParam("id") Long id, Model model) {
        var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        book.getComments().size();
        model.addAttribute("book", book);
        return "book";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        var authors = authorRepository.findAll();
        var genres = genreRepository.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "edit";
    }

    @PostMapping("/edit")
    public String save(@RequestParam("id") Long id,
                       @RequestParam("name") String name,
                       @RequestParam("author") Long authorId,
                       @RequestParam("genre") Long genreId,
                       Model model) {

        var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        var genre = genreRepository.findById(genreId).orElseThrow(NotFoundException::new);
        var author = authorRepository.findById(authorId).orElseThrow(NotFoundException::new);

        book.setName(name);
        book.setAuthor(author);
        book.setGenre(genre);

        bookRepository.save(book);

        return "redirect:/books/view?id=" + book.getId();
    }

}
