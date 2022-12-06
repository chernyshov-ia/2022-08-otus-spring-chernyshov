package ru.otus.books.services;

import org.springframework.stereotype.Service;
import ru.otus.books.domain.Book;
import ru.otus.books.domain.BookComment;
import ru.otus.books.repositories.BookCommentRepository;
import ru.otus.books.repositories.BookRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BookCommentServiceImpl implements BookCommentService{
    private final BookCommentRepository repository;
    private final BookRepository bookRepository;

    public BookCommentServiceImpl(BookCommentRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookComment> findByBookId(long bookId) {
        return repository.findByBookId(bookId);
    }

    @Override
    public Optional<BookComment> findById(long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public Optional<BookComment> updateTextById(long id, String text) {
        Optional<BookComment> optionalComment = repository.findById(id);
        if(optionalComment.isEmpty()){
            return Optional.empty();
        } else {
            var book = optionalComment.orElseThrow(() -> new BookServiceException("Comment not found"));
            book.setText(text);
            return Optional.of(repository.save(book));
        }
    }

    @Transactional
    @Override
    public Optional<BookComment> create(String text, long bookId) {
        if (text == null || text.equals("")) {
            return Optional.empty();
        }

        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            return Optional.empty();
        }

        Book book = optionalBook.orElseThrow(() -> new BookServiceException("Book not found"));
        BookComment comment = new BookComment(text, book);

        return Optional.of(repository.save(comment));
    }
}
