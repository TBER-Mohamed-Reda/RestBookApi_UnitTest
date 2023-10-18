package com.tber.biblioEvaluation.servicesTest;

import com.tber.biblioEvaluation.entities.Book;
import com.tber.biblioEvaluation.exceptions.BookNotFoundException;
import com.tber.biblioEvaluation.repositories.BookRepository;
import com.tber.biblioEvaluation.services.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void should_return_book_by_id() {
        Long bookId = 1L;
        Book expectedBook = Book.builder().id(bookId).label("game of thrones").isbn("1254679451").nbPages(100).build();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));
        Book actualBook = bookService.getBookById(bookId);
        Assertions.assertEquals(expectedBook, actualBook);
    }

    @Test
    public void should_return_lis_of_books() {
        Book book1 = Book.builder().id(1L).label("game of thrones").isbn("1254679451").nbPages(100).build();
        Book book2 = Book.builder().id(2L).label("les miserables").isbn("3334679451").nbPages(200).build();
        List<Book> expectedListOfBooks = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(expectedListOfBooks);
        List<Book> actualListOfBooks = bookService.listOfBooks();
        Assertions.assertEquals(expectedListOfBooks, actualListOfBooks);
    }

    @Test
    public void should_add_book() {
        Book book = Book.builder().label("game of thrones").isbn("1254679451").nbPages(100).build();
        Book expectedBook = Book.builder().id(1L).label("game of thrones").isbn("1254679451").nbPages(100).build();
        when(bookRepository.save(book)).thenReturn(expectedBook);
        Book actualBook = bookService.addBook(book);
        Assertions.assertEquals(expectedBook, actualBook);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void should_update_book_by_id() {
        Long bookId = 1L;
        Book existingBook = Book.builder().id(bookId).label("game of thrones").isbn("1254679451").nbPages(100).build();
        Book updatedBook = Book.builder().id(bookId).label("lost").isbn("125679451").nbPages(200).build();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        Book result = bookService.updateBook(updatedBook);
        Assertions.assertEquals(updatedBook.getLabel(), result.getLabel());
    }

    @Test
    public void should_delete_book_by_id() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);
        long result = bookService.deleteBookById(bookId);
        Assertions.assertEquals(bookId, result);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void should_throw_exception_when_deleting_non_existing_book() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);
        Assertions.assertThrows(BookNotFoundException.class, () -> bookService.deleteBookById(bookId));
    }
}
