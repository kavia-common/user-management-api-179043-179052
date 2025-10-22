package com.example.usermanagementbackend.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user profile update requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {
    
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;
    
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private String password;
}
