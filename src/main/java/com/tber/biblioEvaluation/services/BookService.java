package com.tber.biblioEvaluation.services;

import com.tber.biblioEvaluation.entities.Book;

import java.util.List;

public interface BookService {
    List<Book> listOfBooks();

    Book updateBook(Book book);
    long deleteBookById(Long id);
    Book getBookById(Long id);
    Book addBook(Book book);


}
