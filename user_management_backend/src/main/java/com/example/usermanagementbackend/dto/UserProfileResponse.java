package com.example.usermanagementbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user profile responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    
    private Long id;
    private String email;
    private String fullName;
    private String authProvider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
