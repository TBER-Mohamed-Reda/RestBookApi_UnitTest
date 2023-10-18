package com.tber.biblioEvaluation.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tber.biblioEvaluation.entities.Book;
import com.tber.biblioEvaluation.exceptions.BookNotFoundException;
import com.tber.biblioEvaluation.services.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookServiceImpl bookService;

    @Test
    public void should_return_list_of_all_books() throws Exception {
        List<Book> bookList = Arrays.asList(
                Book.builder().id(1L).label("game of thrones").isbn("15450241245").nbPages(100).build(),
                Book.builder().id(2L).label("les miserables").isbn("15450111245").nbPages(200).build(),
                Book.builder().id(3L).label("lost").isbn("15222241245").nbPages(1000).build()
        );
        when(bookService.listOfBooks()).thenReturn(bookList);
        String responseBody = objectMapper.writeValueAsString(bookList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(content().json(responseBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void should_return_book_by_id_if_exists() throws Exception {
        long bookId = 1L;
        Book book=Book.builder().id(1L).label("game of thrones").isbn("15450241245").nbPages(100).build();
        when(bookService.getBookById(bookId)).thenReturn(book);
        String responseBody = objectMapper.writeValueAsString(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{bookId}", bookId))
                .andExpect(content().json(responseBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(jsonPath("$.nbPages").value(book.getNbPages()))
                .andExpect(jsonPath("$.label").value(book.getLabel()));
    }

    @Test
    public void should_return_404_if_book_does_not_exist() throws Exception {
        long bookId = 20L;
        when(bookService.getBookById(bookId)).thenThrow(new BookNotFoundException("Book not found with id: " + bookId));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{bookId}", bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_Return_new_book_when_book_is_valid() throws Exception {
        Book newBook=Book.builder().label("game of thrones").isbn("15450241245").nbPages(100).build();
        Book createdBook = Book.builder().id(1L).label("game of thrones").isbn("15450241245").nbPages(100).build();
        given(bookService.addBook(newBook)).willReturn(createdBook);
        String requestBody = objectMapper.writeValueAsString(newBook);
        String responseBody = objectMapper.writeValueAsString(createdBook);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseBody))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.label", is(newBook.getLabel())))
                .andExpect(jsonPath("$.isbn", is(newBook.getIsbn())));
    }


    @Test
    public void should_return_updated_book_when_book_exists() throws Exception {
        Book updatedBook = Book.builder().id(1L).label("game of thrones").isbn("15450241245").nbPages(100).build();
        when(bookService.updateBook(updatedBook)).thenReturn(updatedBook);
        String requestBody = objectMapper.writeValueAsString(updatedBook);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.label", is(updatedBook.getLabel())))
                .andExpect(jsonPath("$.isbn", is(updatedBook.getIsbn())));
    }


    @Test
    public void should_return_book_if_exists() throws Exception {
        long bookId = 1L;
        when(bookService.deleteBookById(bookId)).thenReturn(bookId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/{id}", bookId))
                .andExpect(status().isNoContent());
    }
}
