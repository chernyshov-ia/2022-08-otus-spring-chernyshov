package ru.otus.books.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.books.rest.dto.BookDto;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("circuitBreakerBookService")
public class CircuitBreakerBookService implements BookService {
    private final BookService bookService;

    public CircuitBreakerBookService(@Qualifier("bookService") BookService bookService) {
        this.bookService = bookService;
    }

    @HystrixCommand(commandKey = "findBookById", fallbackMethod = "getBookFallback")
    @Override
    public Optional<BookDto> findById(Long id) {
        return bookService.findById(id);
    }

    Optional<BookDto> getBookFallback(Long id) {
        return null;
    }


    @HystrixCommand(commandKey = "findAllBooks", fallbackMethod = "noBooksDtoFallback")
    @Override
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    private List<BookDto> noBooksDtoFallback() {
        return List.of();
    }

    @HystrixCommand(commandKey = "deleteBookById")
    @Override
    public void deleteById(Long id) {
        bookService.deleteById(id);
    }

    @HystrixCommand(commandKey = "saveBook")
    @Override
    public BookDto save(BookDto book) {
        return bookService.save(book);
    }



}
