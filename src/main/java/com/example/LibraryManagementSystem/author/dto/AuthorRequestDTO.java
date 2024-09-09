package com.example.LibraryManagementSystem.author.dto;

import com.example.LibraryManagementSystem.book.dto.BookRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorRequestDTO {
    private String name;
    private String biography;
    private List<BookRequestDTO> books;
}
