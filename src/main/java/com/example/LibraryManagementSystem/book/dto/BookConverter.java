package com.example.LibraryManagementSystem.book.dto;

import com.example.LibraryManagementSystem.author.entity.AuthorEntity;
import com.example.LibraryManagementSystem.book.entity.BookEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookConverter {

    public BookRequestDTO convertToEntity(BookResponseDTO bookResponseDTO) {
        BookRequestDTO book = new BookRequestDTO();
        book.setTitle(bookResponseDTO.getTitle());
        book.setIsbn(bookResponseDTO.getIsbn());
        book.setDescription(bookResponseDTO.getDescription());
        return book;
    }

    public static BookResponseDTO convertToDTO(BookEntity bookEntity) {
        if (bookEntity == null) return null;
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setId(bookEntity.getId());
        bookResponseDTO.setTitle(bookEntity.getTitle());
        bookResponseDTO.setIsbn(bookEntity.getIsbn());
        bookResponseDTO.setDescription(bookEntity.getDescription());
        if (bookEntity.getAuthorEntity() != null) {
            bookResponseDTO.setAuthorName(bookEntity.getAuthorEntity().getName());
        }
        return bookResponseDTO;
    }

    public static List<BookEntity> convertToBookList(List<BookRequestDTO> bookRequestDTOList, AuthorEntity authorEntity) {
        return bookRequestDTOList.stream()
                .map(bookRequestDTO -> {
                    BookEntity bookEntity = new BookEntity();
                    bookEntity.setTitle(bookRequestDTO.getTitle());
                    bookEntity.setIsbn(bookRequestDTO.getIsbn());
                    bookEntity.setDescription(bookRequestDTO.getDescription());
                    bookEntity.setAuthorEntity(authorEntity);
                    return bookEntity;
                })
                .collect(Collectors.toList());
    }
}
