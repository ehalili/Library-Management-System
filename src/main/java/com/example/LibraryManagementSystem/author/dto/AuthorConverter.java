package com.example.LibraryManagementSystem.author.dto;

import com.example.LibraryManagementSystem.author.entity.AuthorEntity;
import com.example.LibraryManagementSystem.book.dto.BookConverter;
import org.springframework.stereotype.Component;

@Component
public class AuthorConverter {

    public AuthorEntity convertToEntity(AuthorResponseDTO authorDTO) {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(authorDTO.getId());
        authorEntity.setName(authorDTO.getName());
        authorEntity.setBiography(authorDTO.getBiography());
        return authorEntity;
    }

    public AuthorResponseDTO convertToDTO(AuthorEntity authorEntity) {
        if (authorEntity == null) return null;
        AuthorResponseDTO authorDTO = new AuthorResponseDTO();
        authorDTO.setId(authorEntity.getId());
        authorDTO.setName(authorEntity.getName());
        authorDTO.setBiography(authorEntity.getBiography());
        authorDTO.setBooks(authorEntity.getBooks().stream().map(BookConverter::convertToDTO).toList());
        return authorDTO;
    }
}
