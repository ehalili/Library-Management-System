package com.example.LibraryManagementSystem.book.repository;

import com.example.LibraryManagementSystem.book.entity.BookEntity;

import java.util.List;

public interface BookRepository{

    List<BookEntity> findAllBooks();
    BookEntity findBookById(Long id);
    BookEntity findBookByIsbn(String isbn);
    void addBook(BookEntity bookEntity);
    void updateBook(BookEntity bookEntity);
    void deleteBook(Long id);
}
