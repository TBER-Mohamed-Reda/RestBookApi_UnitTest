package com.tber.biblioEvaluation.controllers;

import com.tber.biblioEvaluation.entities.Book;
import com.tber.biblioEvaluation.exceptions.BookNotFoundException;
import com.tber.biblioEvaluation.services.BookServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/books")
public class BookController {
    private final BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAllBooks() {
        return bookService.listOfBooks();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book getOneBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book SaveBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public Book updateBookById(@RequestBody Book book) {
        return bookService.updateBook(book);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public long removeOneBookById(@PathVariable Long id) {
        return bookService.deleteBookById(id);
    }
}
