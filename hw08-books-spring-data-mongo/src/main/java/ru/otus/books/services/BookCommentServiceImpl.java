package ru.otus.books.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.BookComment;
import ru.otus.books.repositories.BookCommentRepository;
import ru.otus.books.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository repository;
    private final BookRepository bookRepository;

    public BookCommentServiceImpl(BookCommentRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookComment> findByBookId(String bookId) {
        var book = bookRepository.findById(bookId).orElseThrow(() -> new BookServiceException("Book not found"));
        return book.getComments();
    }

    @Override
    public Optional<BookComment> findById(String id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        var comment = repository.findById(id).orElseThrow(() -> new BookServiceException("Comment not found"));
        var book = comment.getBook();
        book.getComments().remove(comment);
        bookRepository.save(book);
        repository.deleteById(id);
    }

    @Override
    public Optional<BookComment> updateTextById(String id, String text) {
        Optional<BookComment> optionalComment = repository.findById(id);
        if (optionalComment.isEmpty()) {
            return Optional.empty();
        } else {
            var comment = optionalComment.get();
            comment.setText(text);

            var book = comment.getBook();
            var commentOptional = book.getComments().stream().filter(comment::equals).findFirst();
            if(commentOptional.isPresent()) {
                var bookComment = commentOptional.get();
                bookComment.setText(text);
                bookRepository.save(book);
            }

            return Optional.of(repository.save(comment));
        }
    }

    @Transactional
    @Override
    public Optional<BookComment> create(String text, String bookId) {
        if (text == null || text.equals("")) {
            return Optional.empty();
        }

        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            return Optional.empty();
        }

        Book book = optionalBook.get();

        var comment = repository.save(new BookComment(text, book));

        book.getComments().add(comment);
        bookRepository.save(book);

        return Optional.of(comment);
    }
}
