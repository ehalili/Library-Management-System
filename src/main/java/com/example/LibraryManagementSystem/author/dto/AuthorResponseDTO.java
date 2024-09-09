package com.example.LibraryManagementSystem.author.dto;

import com.example.LibraryManagementSystem.book.dto.BookResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorResponseDTO {
    private Long id;
    private String name;
    private String biography;
    private List<BookResponseDTO> books;
}
