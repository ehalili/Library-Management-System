package com.example.LibraryManagementSystem.author.entity;


import com.example.LibraryManagementSystem.book.entity.BookEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "biography")
    private String biography;

    @OneToMany(mappedBy = "authorEntity", cascade = CascadeType.ALL)
    private List<BookEntity> books = new ArrayList<>();

    @Override
    public String toString() {
        return String.format("Author{id=%d, name='%s', biography='%s', books=%s}", id, name, biography, books);
    }
}
