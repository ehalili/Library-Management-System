package com.example.LibraryManagementSystem.book.controller;

import com.example.LibraryManagementSystem.book.dto.BookRequestDTO;
import com.example.LibraryManagementSystem.book.dto.BookResponseDTO;
import com.example.LibraryManagementSystem.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(description = "Retrieve the details of a book using its unique ID.")
    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        BookResponseDTO book = bookService.findBookById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(description = "Retrieve a list of all books.")
    @GetMapping("/all")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<BookResponseDTO> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(description = "Create a new book with the provided details.")
    @PostMapping
    @PreAuthorize(value = "hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<BookResponseDTO> addBook(@RequestBody @Valid BookRequestDTO bookRequestDTO) {
        BookResponseDTO book = bookService.addBook(bookRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @Operation(description = "Delete a book by its unique ID.")
    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Update the details of an existing book by its unique ID.")
    @PutMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<BookResponseDTO> updateBook(@RequestBody @Valid BookRequestDTO bookRequestDTO, @PathVariable Long id) {
        BookResponseDTO book = bookService.updateBook(id, bookRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }
}
