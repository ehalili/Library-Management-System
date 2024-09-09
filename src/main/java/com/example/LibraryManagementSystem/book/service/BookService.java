package com.example.LibraryManagementSystem.book.service;

import com.example.LibraryManagementSystem.author.entity.AuthorEntity;
import com.example.LibraryManagementSystem.author.repository.AuthorRepository;
import com.example.LibraryManagementSystem.book.dto.BookConverter;
import com.example.LibraryManagementSystem.book.dto.BookRequestDTO;
import com.example.LibraryManagementSystem.book.dto.BookResponseDTO;
import com.example.LibraryManagementSystem.book.entity.BookEntity;
import com.example.LibraryManagementSystem.book.repository.BookRepository;
import com.example.LibraryManagementSystem.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookResponseDTO findBookById(Long id) {
        BookEntity bookEntity = bookRepository.findBookById(id);
        if (bookEntity == null) {
            throw new NotFoundException("Book not found");
        }
        return BookConverter.convertToDTO(bookEntity);
    }

    public List<BookResponseDTO> findAllBooks() {
        return bookRepository.findAllBooks().
                stream().
                map(BookConverter::convertToDTO).toList();
    }

    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO) {

        BookEntity bookByIsbn = bookRepository.findBookByIsbn(bookRequestDTO.getIsbn());

        if (bookByIsbn != null) {
            throw new NotFoundException("Book with ISBN " + bookRequestDTO.getIsbn() + " already exists");
        }

        AuthorEntity author = authorRepository.findAuthorById(bookRequestDTO.getAuthorId());
        if (author == null) {
            throw new NotFoundException("Author with given id " + bookRequestDTO.getAuthorId() + " not found");
        }

        BookEntity bookEntity = new BookEntity();

        bookEntity.setTitle(bookRequestDTO.getTitle());
        bookEntity.setDescription(bookRequestDTO.getDescription());
        bookEntity.setIsbn(bookRequestDTO.getIsbn());
        bookEntity.setAuthorEntity(author);
        bookRepository.addBook(bookEntity);
        return BookConverter.convertToDTO(bookEntity);
    }

    public BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO) {
        BookEntity bookExists = bookRepository.findBookById(id);
        if (bookExists == null) {
            throw new NotFoundException("Book with ID " + id + " not found");
        }
        BookEntity bookByIsbn = bookRepository.findBookByIsbn(bookRequestDTO.getIsbn());

        if (bookByIsbn != null && !bookByIsbn.getId().equals(id)) {
            throw new NotFoundException("Book with ISBN " + bookRequestDTO.getIsbn() + " already exists");
        }

        AuthorEntity author = authorRepository.findAuthorById(bookRequestDTO.getAuthorId());

        if (author == null) {
            throw new NotFoundException("Author with ID " + bookRequestDTO.getAuthorId() + " not found");
        }

        bookExists.setTitle(bookRequestDTO.getTitle());
        bookExists.setDescription(bookRequestDTO.getDescription());
        bookExists.setIsbn(bookRequestDTO.getIsbn());
        bookExists.setAuthorEntity(author);

        bookRepository.updateBook(bookExists);

        return BookConverter.convertToDTO(bookExists);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteBook(id);
    }


}
