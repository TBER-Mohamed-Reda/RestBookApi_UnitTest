package com.tber.biblioEvaluation.services;

import com.tber.biblioEvaluation.entities.Book;
import com.tber.biblioEvaluation.exceptions.BookNotFoundException;
import com.tber.biblioEvaluation.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public List<Book> listOfBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book updateBook(Book book) {
        Optional<Book> optionalBook = bookRepository.findById(book.getId());
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book not found with id: " + book.getId());
        }
        Book existingBook = Book.builder().id(book.getId()).label(book.getLabel()).nbPages(book.getNbPages()).isbn(book.getIsbn()).build();
        return bookRepository.save(existingBook);
    }


    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public long deleteBookById(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return id;
        } else {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }
}
