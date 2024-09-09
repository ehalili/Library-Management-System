package com.example.LibraryManagementSystem;

import com.example.LibraryManagementSystem.author.dto.AuthorConverter;
import com.example.LibraryManagementSystem.author.dto.AuthorRequestDTO;
import com.example.LibraryManagementSystem.author.dto.AuthorResponseDTO;
import com.example.LibraryManagementSystem.author.entity.AuthorEntity;
import com.example.LibraryManagementSystem.author.repository.AuthorRepository;
import com.example.LibraryManagementSystem.author.service.AuthorService;
import com.example.LibraryManagementSystem.book.dto.BookConverter;
import com.example.LibraryManagementSystem.book.dto.BookRequestDTO;
import com.example.LibraryManagementSystem.book.dto.BookResponseDTO;
import com.example.LibraryManagementSystem.book.entity.BookEntity;
import com.example.LibraryManagementSystem.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorConverter authorConverter;

    @InjectMocks
    private AuthorService authorService;

    private AuthorEntity authorEntity;
    private AuthorRequestDTO authorRequestDTO;
    private AuthorResponseDTO authorResponseDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(1L);
        bookEntity.setTitle("Book Title");
        bookEntity.setDescription("Book Description");
        bookEntity.setIsbn("1234567890");

        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("Book Title");
        bookRequestDTO.setDescription("Book Description");
        bookRequestDTO.setIsbn("1234567890");

        authorEntity = new AuthorEntity();
        authorEntity.setId(1L);
        authorEntity.setName("Author Name");
        authorEntity.setBiography("Author Biography");
        authorEntity.setBooks(Collections.singletonList(bookEntity));

        authorRequestDTO = new AuthorRequestDTO();
        authorRequestDTO.setName("Author Name");
        authorRequestDTO.setBiography("Author Biography");
        authorRequestDTO.setBooks(Collections.singletonList(bookRequestDTO));

        AuthorEntity conflictingAuthorEntity = new AuthorEntity();
        conflictingAuthorEntity.setId(2L);
        conflictingAuthorEntity.setName("Author Name");
        conflictingAuthorEntity.setBiography("Conflicting Author Biography");

        authorResponseDTO = new AuthorResponseDTO();
        authorResponseDTO.setId(1L);
        authorResponseDTO.setName("Author Name");
        authorResponseDTO.setBiography("Author Biography");
        BookResponseDTO bookResponseDTO = BookConverter.convertToDTO(bookEntity);
        authorResponseDTO.setBooks(Collections.singletonList(bookResponseDTO));

    }


    @Test
    public void testFindAuthorById_Success() {
        when(authorRepository.findAuthorById(1L)).thenReturn(authorEntity);
        when(authorConverter.convertToDTO(authorEntity)).thenReturn(authorResponseDTO);

        AuthorResponseDTO response = authorService.findAuthorById(1L);

        assertNotNull(response);
        assertEquals("Author Name", response.getName());
        verify(authorRepository, times(1)).findAuthorById(1L);
        verify(authorConverter, times(1)).convertToDTO(authorEntity);
    }

    @Test
    public void testFindAuthorById_NotFound() {
        when(authorRepository.findAuthorById(1L)).thenReturn(null);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            authorService.findAuthorById(1L);
        });

        assertEquals("Author not found", thrown.getMessage());
    }

    @Test
    public void testAddAuthor_Success() {
        // Arrange
        when(authorRepository.findAuthorByName("Author Name")).thenReturn(null);
        doNothing().when(authorRepository).addAuthor(any(AuthorEntity.class));
        when(authorConverter.convertToDTO(any(AuthorEntity.class))).thenReturn(authorResponseDTO);

        AuthorResponseDTO response = authorService.addAuthor(authorRequestDTO);

        assertNotNull(response);
        assertEquals("Author Name", response.getName());
        verify(authorRepository, times(1)).findAuthorByName("Author Name");
        verify(authorRepository, times(1)).addAuthor(any(AuthorEntity.class));
        verify(authorConverter, times(1)).convertToDTO(any(AuthorEntity.class));
    }

    @Test
    public void testAddAuthor_Conflict() {
        when(authorRepository.findAuthorByName("Author Name")).thenReturn(authorEntity);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            authorService.addAuthor(authorRequestDTO);
        });

        assertEquals("Author with name Author Name already exists", thrown.getMessage());
    }

    @Test
    public void testFindAllAuthors() {
        when(authorRepository.findAllAuthors()).thenReturn(Collections.singletonList(authorEntity));
        when(authorConverter.convertToDTO(authorEntity)).thenReturn(authorResponseDTO);

        List<AuthorResponseDTO> response = authorService.findAllAuthors();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals("Author Name", response.get(0).getName());
        verify(authorRepository, times(1)).findAllAuthors();
        verify(authorConverter, times(1)).convertToDTO(authorEntity);
    }

    @Test
    public void testDeleteAuthorById() {
        doNothing().when(authorRepository).deleteAuthor(1L);

        assertDoesNotThrow(() -> authorService.deleteAuthorById(1L));

        verify(authorRepository, times(1)).deleteAuthor(1L);
    }

    @Test
    public void testUpdateAuthor_Success() {
        when(authorRepository.findAuthorById(1L)).thenReturn(authorEntity);
        when(authorRepository.findAuthorByName("Author Name")).thenReturn(null);
        when(authorConverter.convertToDTO(authorEntity)).thenReturn(authorResponseDTO);

        AuthorResponseDTO response = authorService.updateAuthor(authorRequestDTO, 1L);

        assertNotNull(response);
        assertEquals("Author Name", response.getName());
        verify(authorRepository, times(1)).findAuthorById(1L);
        verify(authorRepository, times(1)).findAuthorByName("Author Name");
        verify(authorRepository, times(1)).updateAuthor(authorEntity);
        verify(authorConverter, times(1)).convertToDTO(authorEntity);
    }

    @Test
    public void testUpdateAuthor_NotFound() {
        when(authorRepository.findAuthorById(1L)).thenReturn(null);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            authorService.updateAuthor(authorRequestDTO, 1L);
        });

        assertEquals("Author not found", thrown.getMessage());
    }

    @Test
    public void testUpdateAuthor_Conflict() {
        when(authorRepository.findAuthorById(1L)).thenReturn(authorEntity);
        when(authorRepository.findAuthorByName("Author Name")).thenReturn(new AuthorEntity(2L, "Author Name", "Conflicting Author Biography", null)); // Simulating conflict

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            authorService.updateAuthor(authorRequestDTO, 1L);
        });

        assertEquals("Author with name Author Name already exists", thrown.getMessage());
    }

}

