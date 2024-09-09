package com.example.LibraryManagementSystem;

import com.example.LibraryManagementSystem.author.dto.AuthorRequestDTO;
import com.example.LibraryManagementSystem.author.dto.AuthorResponseDTO;
import com.example.LibraryManagementSystem.security.auth.AuthenticationRequest;
import com.example.LibraryManagementSystem.security.auth.AuthenticationResponse;
import com.example.LibraryManagementSystem.security.user.Role;
import com.example.LibraryManagementSystem.security.user.User;
import com.example.LibraryManagementSystem.security.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IntegrationTesting extends BaseTest{

    AuthorRequestDTO authorRequestDTO;
    @Autowired
    UserRepository repository;
    @Autowired
    PasswordEncoder encoder;

    Map<String, String> headers = new HashMap<>();

    @PostConstruct
    public void register() {
        var user = new User();
            user.setEmail("eronnhalili@gmail.com");
            user.setPassword(encoder.encode("123123"));
            user.setRole(Role.ADMIN);
        repository.save(user);
    }

    @BeforeEach
    public void login() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("eronnhalili@gmail.com");
        request.setPassword("123123");
        AuthenticationResponse response = postAndExpectOne("api/v1/auth/authenticate", request, null, AuthenticationResponse.class);
        token = response.getAccessToken();
    }

    @Test
    public void addAuthor() {
        authorRequestDTO = new AuthorRequestDTO();
        authorRequestDTO.setName("Eron");
        authorRequestDTO.setBiography("test");
        authorRequestDTO.setBooks(new ArrayList<>());

        headers.put("Authorization", "Bearer " + token);

        AuthorResponseDTO authorResponseDTO = postAndExpectOne("/api/v1/author", authorRequestDTO, headers, AuthorResponseDTO.class);

        assertThat(authorResponseDTO.getId()).isEqualTo(1L);
        assertThat(authorResponseDTO.getName()).isEqualTo("Eron");
    }

}
