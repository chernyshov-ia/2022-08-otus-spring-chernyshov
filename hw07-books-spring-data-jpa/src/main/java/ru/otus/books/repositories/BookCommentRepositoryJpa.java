package ru.otus.books.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.books.domain.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookCommentRepositoryJpa implements BookCommentRepository {
    @PersistenceContext
    private final EntityManager em;

    public BookCommentRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<BookComment> findByBookId(long bookId) {
        TypedQuery<BookComment> query = em.createQuery("select s from BookComment s join s.book b where b.id = :id", BookComment.class);
        query.setParameter("id", bookId);
        return query.getResultList();
    }

    @Override
    public Optional<BookComment> findById(long id) {
        return Optional.ofNullable(em.find(BookComment.class, id));
    }

    @Override
    public BookComment save(BookComment comment) {
        if (comment.getId() <= 0) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public void deleteById(long id) {
//        t = em.merge(t);
//        em.remove(t);

        Query query = em.createQuery("delete from BookComment s where s.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}

