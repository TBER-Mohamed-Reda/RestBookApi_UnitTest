package com.tber.biblioEvaluation.repositories;

import com.tber.biblioEvaluation.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    long deleteBookById(Long id);
}
