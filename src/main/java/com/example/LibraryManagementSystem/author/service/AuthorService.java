package com.example.LibraryManagementSystem.author.service;

import com.example.LibraryManagementSystem.author.dto.AuthorConverter;
import com.example.LibraryManagementSystem.author.dto.AuthorRequestDTO;
import com.example.LibraryManagementSystem.author.dto.AuthorResponseDTO;
import com.example.LibraryManagementSystem.author.entity.AuthorEntity;
import com.example.LibraryManagementSystem.author.repository.AuthorRepository;
import com.example.LibraryManagementSystem.book.dto.BookConverter;
import com.example.LibraryManagementSystem.book.entity.BookEntity;
import com.example.LibraryManagementSystem.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorConverter authorConverter;

    public AuthorResponseDTO findAuthorById(Long id) {
        AuthorEntity authorEntity = authorRepository.findAuthorById(id);
        if (authorEntity == null) {
            throw new NotFoundException("Author not found");
        }
        return authorConverter.convertToDTO(authorEntity);
    }

    public AuthorResponseDTO addAuthor(AuthorRequestDTO authorRequestDTO) {

        AuthorEntity authorEntityWithName = authorRepository.findAuthorByName(authorRequestDTO.getName());

        if (authorEntityWithName != null) {
            throw new NotFoundException("Author with name " + authorRequestDTO.getName() + " already exists");
        }

        AuthorEntity newAuthorEntity = new AuthorEntity();

        newAuthorEntity.setName(authorRequestDTO.getName());
        newAuthorEntity.setBiography(authorRequestDTO.getBiography());

        if (authorRequestDTO.getBooks() != null) {
            List<BookEntity> bookEntities =
                    BookConverter.convertToBookList(authorRequestDTO.getBooks(), newAuthorEntity);
            newAuthorEntity.setBooks(bookEntities);
        }
        authorRepository.addAuthor(newAuthorEntity);
        return authorConverter.convertToDTO(newAuthorEntity);
    }

    public List<AuthorResponseDTO> findAllAuthors() {
        List<AuthorEntity> authorEntities = authorRepository.findAllAuthors();
        return authorEntities.stream().map(authorConverter::convertToDTO).toList();
    }

    public void deleteAuthorById(Long id) {
        authorRepository.deleteAuthor(id);
    }

    public AuthorResponseDTO updateAuthor(AuthorRequestDTO authorRequestDTO, Long id) {
        AuthorEntity existingAuthorEntity = authorRepository.findAuthorById(id);

        if (existingAuthorEntity == null) {
            throw new NotFoundException("Author not found");
        }

        AuthorEntity authorEntityWithName = authorRepository.findAuthorByName(authorRequestDTO.getName());
        if (authorEntityWithName != null && !authorEntityWithName.getId().equals(id)) {
            throw new NotFoundException("Author with name " + authorRequestDTO.getName() + " already exists");
        }

        existingAuthorEntity.setName(authorRequestDTO.getName());
        existingAuthorEntity.setBiography(authorRequestDTO.getBiography());

        if (authorRequestDTO.getBooks() != null) {
            List<BookEntity> bookEntities =
                    BookConverter.convertToBookList(authorRequestDTO.getBooks(), existingAuthorEntity);
            existingAuthorEntity.setBooks(bookEntities);
        }

        authorRepository.updateAuthor(existingAuthorEntity);
        return authorConverter.convertToDTO(existingAuthorEntity);
    }
}
