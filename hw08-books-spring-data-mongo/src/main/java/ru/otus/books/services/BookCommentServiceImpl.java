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
public class BookCommentServiceImpl implements BookCommentService{
    private final BookCommentRepository repository;
    private final BookRepository bookRepository;

    public BookCommentServiceImpl(BookCommentRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookComment> findByBookId(String bookId) {
        // return repository.findByBookId(bookId);
        return List.of();
    }

    @Override
    public Optional<BookComment> findById(String id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<BookComment> updateTextById(String id, String text) {
        Optional<BookComment> optionalComment = repository.findById(id);
        if(optionalComment.isEmpty()){
            return Optional.empty();
        } else {
            var book = optionalComment.orElseThrow();
            book.setText(text);
            return Optional.of(repository.save(book));
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

        Book book = optionalBook.orElseThrow();
        BookComment comment = new BookComment(text, book);

        return Optional.of(repository.save(comment));
    }
}
