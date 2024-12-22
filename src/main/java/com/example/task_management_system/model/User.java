package com.example.task_management_system.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data // Lombok annotation for getters, setters, and more
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role; // e.g., "USER", "ADMIN"
}
