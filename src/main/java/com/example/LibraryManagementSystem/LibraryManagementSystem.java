package com.example.LibraryManagementSystem;

import com.example.LibraryManagementSystem.security.auth.AuthenticationService;
import com.example.LibraryManagementSystem.security.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static com.example.LibraryManagementSystem.security.user.Role.ADMIN;
import static com.example.LibraryManagementSystem.security.user.Role.MANAGER;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")

public class LibraryManagementSystem {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementSystem.class, args);
    }
}

