package com.example.LibraryManagementSystem.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String isbn;
    private String authorName;
}
