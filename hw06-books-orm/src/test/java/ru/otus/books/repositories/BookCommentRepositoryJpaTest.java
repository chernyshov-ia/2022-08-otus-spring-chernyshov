package ru.otus.books.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.BookComment;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
@Import(BookCommentRepositoryJpa.class)
class BookCommentRepositoryJpaTest {
    private static final long CHECK_BOOK_ID = 1;
    private static final int EXPECTED_COMMENTS_COUNT_BY_BOOK = 2;
    private static final long FIRST_COMMENT_ID = 1;
    private static final long SECOND_COMMENT_ID = 2;
    private static final String UPDATED_TEXT = "Updated text";


    @Autowired
    private BookCommentRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" должен загружать информацию о нужном комментарии по его id")
    @Test
    void shouldFindExpectedCommentById() {
        var optionalActualComment = repositoryJpa.findById(FIRST_COMMENT_ID);
        var expectedComment = em.find(BookComment.class, FIRST_COMMENT_ID);
        assertThat(optionalActualComment).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsListByBookId() {
        List<BookComment> comments = repositoryJpa.findByBookId(CHECK_BOOK_ID);
        assertThat(comments).isNotNull().hasSize(EXPECTED_COMMENTS_COUNT_BY_BOOK)
                .allMatch(s -> !s.getText().equals(""));

        List<Long> commentsIds = comments.stream().map(BookComment::getId).collect(Collectors.toList());
        assertThat(commentsIds).containsExactlyInAnyOrder(FIRST_COMMENT_ID, SECOND_COMMENT_ID);
    }

    @DisplayName(" должен удалить комментарий по его id")
    @Test
    void shouldDeleteCommentById() {
        var notDeletedComment = em.find(BookComment.class, FIRST_COMMENT_ID);
        em.detach(notDeletedComment);
        assertThat(notDeletedComment).isNotNull();

        repositoryJpa.deleteById(FIRST_COMMENT_ID);

        var deletedComment = em.find(BookComment.class, FIRST_COMMENT_ID);
        assertThat(deletedComment).isNull();
    }

    @DisplayName(" должен сохранить комментарий")
    @Test
    void shouldSaveComment() {
        BookComment comment = em.find(BookComment.class, FIRST_COMMENT_ID);
        Book book = comment.getBook();
        book.getId();

        BookComment newComment = new BookComment("Text of new comment", book);
        var savedComment = repositoryJpa.save(newComment);

        assertThat(savedComment).matches(c -> c.getId() > 0)
                .matches(c -> c.getText().equals(newComment.getText()))
                .matches(c -> c.getBook().getId() == book.getId());

        var loadedComment = em.find(BookComment.class, savedComment.getId());

        assertThat(loadedComment).usingRecursiveComparison().isEqualTo(savedComment);
    }

    @DisplayName(" должен обновить текст комментария")
    @Test
    void shouldUpdateComment() {
        BookComment comment = em.find(BookComment.class, FIRST_COMMENT_ID);
        comment.getBook().getId();
        em.detach(comment);
        comment.setText(UPDATED_TEXT);
        var savedComment = repositoryJpa.save(comment);
        em.flush();
        em.detach(savedComment);

        var loadedComment = em.find(BookComment.class, comment.getId());

        assertThat(comment).usingRecursiveComparison().isEqualTo(loadedComment);
    }


}