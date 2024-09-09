package com.example.LibraryManagementSystem.author.repository;

import com.example.LibraryManagementSystem.author.entity.AuthorEntity;

import java.util.List;
public interface AuthorRepository {

    List<AuthorEntity> findAllAuthors();
    AuthorEntity findAuthorById(Long id);
    AuthorEntity findAuthorByName(String name);
    void addAuthor(AuthorEntity authorEntity);
    void updateAuthor(AuthorEntity authorEntity);
    void deleteAuthor(Long id);
}
