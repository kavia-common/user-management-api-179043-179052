package com.example.usermanagementbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User entity representing a registered user in the system.
 * Supports both standard registration and OAuth2 (Google) authentication.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String email;
    
    @Size(max = 100)
    @Column(name = "full_name")
    private String fullName;
    
    @Size(max = 255)
    @Column(name = "password_hash")
    private String passwordHash;
    
    @Size(max = 50)
    @Column(name = "auth_provider")
    private String authProvider; // LOCAL, GOOGLE
    
    @Size(max = 100)
    @Column(name = "provider_id")
    private String providerId; // OAuth2 provider user ID
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
