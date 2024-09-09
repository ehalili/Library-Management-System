package com.example.LibraryManagementSystem.book.entity;

import com.example.LibraryManagementSystem.author.entity.AuthorEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity authorEntity;
}
