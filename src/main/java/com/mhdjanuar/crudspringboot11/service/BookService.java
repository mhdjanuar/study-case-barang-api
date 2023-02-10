package com.mhdjanuar.crudspringboot11.service;

import java.util.List;

import com.mhdjanuar.crudspringboot11.dto.BookCreateRequestDTO;
import com.mhdjanuar.crudspringboot11.dto.BookDetailResponseDTO;
import com.mhdjanuar.crudspringboot11.dto.BookListResponseDTO;
import com.mhdjanuar.crudspringboot11.dto.bookUpdateRequestDTO;

public interface BookService {
    public List<BookListResponseDTO> findBookAll(String title);

    public void createNewBook(BookCreateRequestDTO dto);

    public BookDetailResponseDTO findBookDetail(Long bookId);

    public void updateBook(Long bookId, bookUpdateRequestDTO dto);

    public void deleteBook(Long bookId);
}
