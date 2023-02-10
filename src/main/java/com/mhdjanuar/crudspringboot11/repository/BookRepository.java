package com.mhdjanuar.crudspringboot11.repository;

import com.mhdjanuar.crudspringboot11.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    //select * from book where title LIKE 'nama';
    List<Book> findAllByTitleLike(String title);
}
