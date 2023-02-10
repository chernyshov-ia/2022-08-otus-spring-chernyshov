package ru.otus.books.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.books.domain.Author;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.Genre;
import ru.otus.books.repositories.BookRepository;
import ru.otus.books.rest.dto.BookDto;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WebFluxTest(controllers = BookRestController.class)
@Import(BookRepository.class)
public class BookRestControllerTest {

    private static final Genre GENRE_1 = new Genre("G1", "fantasy");
    private static final Genre GENRE_2 = new Genre("G2", "novel");

    private static final Author AUTHOR_2 = new Author("A2", "Jack London");
    private static final Author AUTHOR_3 = new Author("A3", "John Ronald Reuel Tolkie");

    private static final Book BOOK_1 = new Book("B1", "Hobbit", AUTHOR_3, GENRE_1);
    private static final Book BOOK_2 = new Book("B2", "The Lord of the Rings", AUTHOR_3, GENRE_1);
    private static final Book BOOK_3 = new Book("B3", "White Fang", AUTHOR_2, GENRE_2);

    @Autowired
    private WebTestClient webClient;

    @MockBean
    BookRepository repository;

    @Test
    @DisplayName("Should get books")
    void shouldGetBooks() {
        Mockito.when(repository.findAll()).thenReturn(Flux.just(BOOK_1, BOOK_2, BOOK_3));

        webClient.get().uri("/api/v1/books")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBodyList(BookDto.class)
                .contains(BookDto.fromDomainObject(BOOK_1), BookDto.fromDomainObject(BOOK_2), BookDto.fromDomainObject(BOOK_3));
    }

}
