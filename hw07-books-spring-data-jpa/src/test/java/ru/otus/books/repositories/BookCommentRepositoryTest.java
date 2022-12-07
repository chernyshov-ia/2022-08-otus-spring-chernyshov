package ru.otus.books.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.books.domain.BookComment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий комментариев к книгам ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookCommentRepositoryTest {
    private static final long COMMENTS_ID_1 = 1;
    private static final long COMMENTS_ID_2 = 2;
    private static final long BOOK_ID = 1;

    @Autowired
    private BookCommentRepository repository;

    // проверка кастомного метода?
    @DisplayName("должен возвращать все комментарии по Id кники")
    @Test
    void shouldFindAllCommentsByBookId() {
        var comment_1 = repository.findById(COMMENTS_ID_1).orElseThrow();
        var comment_2 = repository.findById(COMMENTS_ID_2).orElseThrow();
        List<BookComment> comments = repository.findByBookId(BOOK_ID);
        assertThat(comments).containsExactlyInAnyOrder(comment_1, comment_2);
    }

}