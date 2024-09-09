package com.example.LibraryManagementSystem.author.controller;

import com.example.LibraryManagementSystem.author.dto.AuthorRequestDTO;
import com.example.LibraryManagementSystem.author.dto.AuthorResponseDTO;
import com.example.LibraryManagementSystem.author.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @Operation(description = "Retrieve the details of an author using their unique ID.")
    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable Long id) {
        AuthorResponseDTO author = authorService.findAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @Operation(description = "Retrieve a list of all authors.")
    @GetMapping("/allAuthors")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        List<AuthorResponseDTO> authors = authorService.findAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @Operation(description = "Create a new author with the provided details.")
    @PostMapping
    @PreAuthorize(value = "hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<AuthorResponseDTO> addAuthor(@RequestBody @Valid AuthorRequestDTO authorRequestDTO) {
        AuthorResponseDTO author = authorService.addAuthor(authorRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(author);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @Operation(description = "Delete an author by their unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Update the details of an existing author by their unique ID.")
    @PutMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<AuthorResponseDTO> update(@RequestBody @Valid AuthorRequestDTO authorRequestDTO, @PathVariable Long id) {
        AuthorResponseDTO author = authorService.updateAuthor(authorRequestDTO, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(author);
    }
}
