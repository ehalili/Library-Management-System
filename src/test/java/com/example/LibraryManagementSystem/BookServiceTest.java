package com.example.LibraryManagementSystem;

import com.example.LibraryManagementSystem.author.entity.AuthorEntity;
import com.example.LibraryManagementSystem.author.repository.AuthorRepository;
import com.example.LibraryManagementSystem.book.dto.BookConverter;
import com.example.LibraryManagementSystem.book.dto.BookRequestDTO;
import com.example.LibraryManagementSystem.book.dto.BookResponseDTO;
import com.example.LibraryManagementSystem.book.entity.BookEntity;
import com.example.LibraryManagementSystem.book.repository.BookRepository;
import com.example.LibraryManagementSystem.book.service.BookService;
import com.example.LibraryManagementSystem.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    private BookEntity bookEntity;
    private BookRequestDTO bookRequestDTO;
    private BookResponseDTO bookResponseDTO;
    private AuthorEntity authorEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        authorEntity = new AuthorEntity();
        authorEntity.setId(1L);
        authorEntity.setName("Author Name");

        bookEntity = new BookEntity();
        bookEntity.setId(1L);
        bookEntity.setTitle("Book Title");
        bookEntity.setDescription("Book Description");
        bookEntity.setIsbn("1234567890");
        bookEntity.setAuthorEntity(authorEntity);

        bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("Book Title");
        bookRequestDTO.setDescription("Book Description");
        bookRequestDTO.setIsbn("1234567890");
        bookRequestDTO.setAuthorId(1L);

        bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setId(1L);
        bookResponseDTO.setTitle("Book Title");
        bookResponseDTO.setDescription("Book Description");
        bookResponseDTO.setIsbn("1234567890");
        bookResponseDTO.setAuthorName("Author Name");
    }

    @Test
    public void testFindBookById_Success() {
        try (MockedStatic<BookConverter> mockedBookConverter = mockStatic(BookConverter.class)) {
            mockedBookConverter.when(() -> BookConverter.convertToDTO(bookEntity)).thenReturn(bookResponseDTO);

            when(bookRepository.findBookById(1L)).thenReturn(bookEntity);

            BookResponseDTO response = bookService.findBookById(1L);

            assertNotNull(response);
            assertEquals("Book Title", response.getTitle());

            verify(bookRepository, times(1)).findBookById(1L);

            mockedBookConverter.verify(() -> BookConverter.convertToDTO(bookEntity), times(1));
        }
    }

    @Test
    public void testFindBookById_NotFound() {
        when(bookRepository.findBookById(1L)).thenReturn(null);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookService.findBookById(1L);
        });

        assertEquals("Book not found", thrown.getMessage());
    }

    @Test
    public void testFindAllBooks() {
        try (var mockedBookConverter = mockStatic(BookConverter.class)) {
            mockedBookConverter.when(() -> BookConverter.convertToDTO(bookEntity)).thenReturn(bookResponseDTO);

            when(bookRepository.findAllBooks()).thenReturn(Collections.singletonList(bookEntity));

            List<BookResponseDTO> response = bookService.findAllBooks();

            assertNotNull(response);
            assertFalse(response.isEmpty());
            assertEquals("Book Title", response.get(0).getTitle());
            verify(bookRepository, times(1)).findAllBooks();
            mockedBookConverter.verify(() -> BookConverter.convertToDTO(bookEntity), times(1));
        }
    }


    @Test
    public void testAddBook_Success() {
        try (MockedStatic<BookConverter> mockedBookConverter = mockStatic(BookConverter.class)) {
            mockedBookConverter.when(() -> BookConverter.convertToDTO(any(BookEntity.class))).thenReturn(bookResponseDTO);

            when(bookRepository.findBookByIsbn("1234567890")).thenReturn(null);
            when(authorRepository.findAuthorById(1L)).thenReturn(authorEntity);
            doNothing().when(bookRepository).addBook(any(BookEntity.class));

            BookResponseDTO response = bookService.addBook(bookRequestDTO);

            assertNotNull(response);
            assertEquals("Book Title", response.getTitle());

            verify(bookRepository, times(1)).findBookByIsbn("1234567890");
            verify(authorRepository, times(1)).findAuthorById(1L);
            verify(bookRepository, times(1)).addBook(any(BookEntity.class));

            mockedBookConverter.verify(() -> BookConverter.convertToDTO(any(BookEntity.class)), times(1));
        }
    }


    @Test
    public void testAddBook_IsbnConflict() {
        when(bookRepository.findBookByIsbn("1234567890")).thenReturn(bookEntity);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookService.addBook(bookRequestDTO);
        });

        assertEquals("Book with ISBN 1234567890 already exists", thrown.getMessage());
    }

    @Test
    public void testAddBook_AuthorNotFound() {
        when(bookRepository.findBookByIsbn("1234567890")).thenReturn(null);
        when(authorRepository.findAuthorById(1L)).thenReturn(null);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookService.addBook(bookRequestDTO);
        });

        assertEquals("Author with given id 1 not found", thrown.getMessage());
    }

    @Test
    public void testUpdateBook_Success() {
        try (MockedStatic<BookConverter> mockedBookConverter = mockStatic(BookConverter.class)) {
            mockedBookConverter.when(() -> BookConverter.convertToDTO(any(BookEntity.class))).thenReturn(bookResponseDTO);

            when(bookRepository.findBookById(1L)).thenReturn(bookEntity);
            when(bookRepository.findBookByIsbn("1234567890")).thenReturn(null);
            when(authorRepository.findAuthorById(1L)).thenReturn(authorEntity);
            doNothing().when(bookRepository).updateBook(any(BookEntity.class));

            BookResponseDTO response = bookService.updateBook(1L, bookRequestDTO);

            assertNotNull(response);
            assertEquals("Book Title", response.getTitle());

            verify(bookRepository, times(1)).findBookById(1L);
            verify(bookRepository, times(1)).findBookByIsbn("1234567890");
            verify(authorRepository, times(1)).findAuthorById(1L);
            verify(bookRepository, times(1)).updateBook(any(BookEntity.class));

            mockedBookConverter.verify(() -> BookConverter.convertToDTO(any(BookEntity.class)), times(1));
        }
    }

    @Test
    public void testUpdateBook_NotFound() {
        when(bookRepository.findBookById(1L)).thenReturn(null);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookService.updateBook(1L, bookRequestDTO);
        });

        assertEquals("Book with ID 1 not found", thrown.getMessage());
    }

    @Test
    public void testUpdateBook_IsbnConflict() {

        when(bookRepository.findBookById(1L)).thenReturn(bookEntity);

        BookEntity conflictingBookEntity = new BookEntity();
        conflictingBookEntity.setId(2L);
        conflictingBookEntity.setIsbn("1234567890");

        when(bookRepository.findBookByIsbn("1234567890")).thenReturn(conflictingBookEntity);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookService.updateBook(1L, bookRequestDTO);
        });

        assertEquals("Book with ISBN 1234567890 already exists", thrown.getMessage());
    }

    @Test
    public void testUpdateBook_AuthorNotFound() {
        when(bookRepository.findBookById(1L)).thenReturn(bookEntity);
        when(authorRepository.findAuthorById(1L)).thenReturn(null);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookService.updateBook(1L, bookRequestDTO);
        });

        assertEquals("Author with ID 1 not found", thrown.getMessage());
    }

    @Test
    public void testDeleteBookById() {
        doNothing().when(bookRepository).deleteBook(1L);

        assertDoesNotThrow(() -> bookService.deleteBookById(1L));

        verify(bookRepository, times(1)).deleteBook(1L);
    }
}
