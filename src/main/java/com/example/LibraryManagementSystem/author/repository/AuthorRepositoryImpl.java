package com.example.LibraryManagementSystem.author.repository;

import com.example.LibraryManagementSystem.author.entity.AuthorEntity;
import com.example.LibraryManagementSystem.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<AuthorEntity> findAllAuthors() {
        TypedQuery<AuthorEntity> query = entityManager.createQuery("SELECT a FROM AuthorEntity a", AuthorEntity.class);
        return query.getResultList();
    }

    public AuthorEntity findAuthorById(Long id) {
        try {
            TypedQuery<AuthorEntity> query = entityManager.createQuery("SELECT author FROM AuthorEntity author WHERE author.id = :id ", AuthorEntity.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("Author not found");
        }
    }

    public void addAuthor(AuthorEntity authorEntity) {
        entityManager.persist(authorEntity);
    }

    public void updateAuthor(AuthorEntity authorEntity) {
        AuthorEntity authorEntityById = findAuthorById(authorEntity.getId());
        if (authorEntityById == null) {
            throw new NotFoundException("Author not found");
        }
        entityManager.merge(authorEntity);
    }

    public void deleteAuthor(Long id) {
        AuthorEntity authorEntityById = findAuthorById(id);
        if (authorEntityById == null) {
            throw new NotFoundException("Author not found");
        }
        entityManager.remove(authorEntityById);
    }

    public AuthorEntity findAuthorByName(String name) {
        try {
            TypedQuery<AuthorEntity> query = entityManager.createQuery("select a from AuthorEntity a where a.name = :name", AuthorEntity.class);
            return query.setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
