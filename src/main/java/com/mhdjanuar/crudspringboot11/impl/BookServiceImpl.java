package com.mhdjanuar.crudspringboot11.impl;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import antlr.StringUtils;
import com.mhdjanuar.crudspringboot11.dto.BookDetailResponseDTO;
import com.mhdjanuar.crudspringboot11.dto.bookUpdateRequestDTO;
import com.mhdjanuar.crudspringboot11.exception.ResourceNotFoundException;
import com.mhdjanuar.crudspringboot11.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhdjanuar.crudspringboot11.dto.BookCreateRequestDTO;
import com.mhdjanuar.crudspringboot11.dto.BookListResponseDTO;
import com.mhdjanuar.crudspringboot11.service.BookService;
import com.mhdjanuar.crudspringboot11.domain.Book;
import org.springframework.util.ObjectUtils;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<BookListResponseDTO> findBookAll(String title) {
        title = ObjectUtils.isEmpty(title) ? "%": "%"+title+"%";

        List<Book> bookResponses =  bookRepository.findAllByTitleLike(title);

        return bookResponses.stream().map((b) -> {
            BookListResponseDTO dto = new BookListResponseDTO();
            dto.setTitle(b.getTitle());
            dto.setAuthor(b.getAuthor());
            dto.setId(b.getId());
            dto.setDescription(b.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void createNewBook(BookCreateRequestDTO dto) {
        Book book = new Book();
        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        
        bookRepository.save(book);
    }

    @Override
    public BookDetailResponseDTO findBookDetail(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new ResourceNotFoundException("book.not.found"));
        BookDetailResponseDTO dto = new BookDetailResponseDTO();
        dto.setAuthor(book.getAuthor());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        return dto;
    }

    @Override
    public void updateBook(Long bookId, bookUpdateRequestDTO dto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new ResourceNotFoundException("book.not.found"));
        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());

        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }
}
