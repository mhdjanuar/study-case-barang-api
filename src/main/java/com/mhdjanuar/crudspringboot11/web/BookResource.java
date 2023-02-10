package com.mhdjanuar.crudspringboot11.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.mhdjanuar.crudspringboot11.dto.bookUpdateRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mhdjanuar.crudspringboot11.dto.BookListResponseDTO;
import com.mhdjanuar.crudspringboot11.dto.BookCreateRequestDTO;
import com.mhdjanuar.crudspringboot11.dto.BookDetailResponseDTO;
import com.mhdjanuar.crudspringboot11.service.BookService;

@RestController
public class BookResource {

    @Autowired
    private BookService bookService;

    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookDetailResponseDTO> findBookDetail(@PathVariable("bookId") Long bookId){
        BookDetailResponseDTO dto = bookService.findBookDetail(bookId);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/book")
    public ResponseEntity<Void> createNewBook(@RequestBody BookCreateRequestDTO dto) throws URISyntaxException {
        bookService.createNewBook(dto);

        return ResponseEntity.created(new URI("/book")).build();
    }

    @GetMapping("/book")
    public ResponseEntity<List<BookListResponseDTO>> findBookAll(@RequestParam(value = "title",
            required = false) String title){
        List<BookListResponseDTO> dtos = bookService.findBookAll(title);

        return ResponseEntity.ok().body(dtos);
    }
    @PutMapping("/book/{bookId}")
    public ResponseEntity<Void> updateBook(@PathVariable("bookId") Long bookId,
                                           @RequestBody bookUpdateRequestDTO dto) {
        bookService.updateBook(bookId, dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") Long bookId){
        bookService.deleteBook((bookId));

        return ResponseEntity.ok().build();
    }
}
