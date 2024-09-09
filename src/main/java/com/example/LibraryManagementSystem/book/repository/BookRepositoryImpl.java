package com.example.LibraryManagementSystem.book.repository;

import com.example.LibraryManagementSystem.book.entity.BookEntity;
import com.example.LibraryManagementSystem.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<BookEntity> findAllBooks() {
        return entityManager.createQuery("select b from BookEntity b", BookEntity.class).getResultList();
    }

    public BookEntity findBookById(Long id) {
        try {
            TypedQuery<BookEntity> query = entityManager.createQuery("SELECT book FROM BookEntity book WHERE book.id = :id ", BookEntity.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("Book not found");
        }
    }

    public void addBook(BookEntity bookEntity) {
        entityManager.persist(bookEntity);
    }

    public void updateBook(BookEntity entity) {
        BookEntity bookEntity = findBookById(entity.getId());
        if (bookEntity == null) {
            throw new NotFoundException("Book not found");
        }
        entityManager.merge(bookEntity);
    }

    public void deleteBook(Long id) {
        BookEntity bookEntity = findBookById(id);
        if (bookEntity == null) {
            throw new NotFoundException("Book not found");
        }
        entityManager.remove(bookEntity);
    }

    @Override
    public BookEntity findBookByIsbn(String isbn) {
        try {
            TypedQuery<BookEntity> query = entityManager.createQuery("SELECT book FROM BookEntity book WHERE book.isbn = :isbn", BookEntity.class);
            query.setParameter("isbn", isbn);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
