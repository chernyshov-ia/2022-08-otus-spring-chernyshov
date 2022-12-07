package ru.otus.books.repositories;


import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.books.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import(BookRepositoryJpa.class)
public class BookRepositoryJpaTest {
    private static final long EXISTING_BOOK_ID_1 = 1;
    private static final long EXISTING_BOOK_ID_2 = 2;
    private static final int EXPECTED_BOOK_COUNT = 2;
    private static final int EXPECTED_QUERIES_COUNT = 2;
    private static final String UPDATED_NAME = "Updated name";

    @Autowired
    private BookRepositoryJpa repositoryJpa;


    @Autowired
    private TestEntityManager em;

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectedBookById() {
        var expectedBook = em.find(Book.class, EXISTING_BOOK_ID_1);
        expectedBook.getComments().size();
        em.detach(expectedBook);
        var actualBook = repositoryJpa.findById(EXISTING_BOOK_ID_1);
        assertThat(actualBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("возвращать ожидаемый список книг")
    @Test
    void shouldReturnExpectedBookList() {
        List<Book> actualBookList = repositoryJpa.findAll();
        assertThat(actualBookList.stream().map(Book::getId).collect(Collectors.toList()))
                .containsExactlyInAnyOrder(EXISTING_BOOK_ID_1, EXISTING_BOOK_ID_2);
    }

    @DisplayName("возвращать ожидаемый список c полной и информацией")
    @Test
    void shouldReturnExpectedBookListWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        List<Book> actualBookList = repositoryJpa.findAll();

        System.out.println("\n\n\n\n----------------------------------------------------------------------------------------------------------");
        assertThat(actualBookList).isNotNull().hasSize(EXPECTED_BOOK_COUNT)
                .allMatch(s -> !s.getName().equals(""))
                .allMatch(s -> s.getAuthor() != null && s.getAuthor().getId() > 0)
                .allMatch(s -> s.getGenre() != null && s.getGenre().getId() > 0)
                .allMatch(s -> s.getComments() != null && s.getComments().size() >= 0);
        System.out.println("----------------------------------------------------------------------------------------------------------\n\n\n\n");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @DisplayName(" должен удалить комментарий по его id")
    @Test
    void shouldDeleteCommentById() {
        // TODO: как правильно удалить вместе с комметариями, если не использовать ON DELETE CASCADE?

        System.out.println("----- notDeletedBook ------");
        var notDeletedBook = em.find(Book.class, EXISTING_BOOK_ID_1);
        em.detach(notDeletedBook);
        assertThat(notDeletedBook).isNotNull();

        System.out.println("----- deleteById ------");
        repositoryJpa.deleteById(EXISTING_BOOK_ID_1);


        System.out.println("----- deletedBook ------");
        var deletedBook = em.find(Book.class, EXISTING_BOOK_ID_1);
        assertThat(deletedBook).isNull();
    }

    @DisplayName(" добавлять новую книгу в БД")
    @Test
    void shouldInsertBook() {
        /* TODO: javax.persistence.PersistenceException: org.hibernate.PersistentObjectException: detached entity passed to persist: ru.otus.books.domain.Author
            что делать с такой ошибкой? похоже  Author и Genre detached, мне их нужно загружать из em чтобы создать book для сохранения?
        */
        Book exampleBook = em.find(Book.class, EXISTING_BOOK_ID_1);
        Book expectedBook = new Book("New book", exampleBook.getAuthor(), exampleBook.getGenre(), null);
        var savedBook = repositoryJpa.save(expectedBook);
        Book actualBook = em.find(Book.class, savedBook.getId());
        assertThat(actualBook).isNotNull().usingRecursiveComparison().isEqualTo(savedBook);
    }

    @DisplayName(" обновить книгу в БД")
    @Test
    void shouldUpdateBook() {
        Book book = em.find(Book.class, EXISTING_BOOK_ID_1);
        book.getComments().size();
        book.setName(UPDATED_NAME);
        em.detach(book);
        var savedBook = repositoryJpa.save(book);
        em.flush();
        em.detach(savedBook);

        System.out.println("actualBook");
        Book actualBook = em.find(Book.class, book.getId());
        assertThat(actualBook).isNotNull().usingRecursiveComparison().isEqualTo(book);
    }

}
