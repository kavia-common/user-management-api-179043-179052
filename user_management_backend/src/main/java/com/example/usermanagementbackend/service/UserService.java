package com.example.usermanagementbackend.service;

import com.example.usermanagementbackend.dto.UserProfileRequest;
import com.example.usermanagementbackend.dto.UserProfileResponse;
import com.example.usermanagementbackend.exception.BadRequestException;
import com.example.usermanagementbackend.exception.NotFoundException;
import com.example.usermanagementbackend.model.User;
import com.example.usermanagementbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for user profile management operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Get user profile by user ID.
     *
     * @param userId the user ID
     * @return user profile response
     * @throws NotFoundException if user not found
     */
    // PUBLIC_INTERFACE
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        
        return mapToProfileResponse(user);
    }
    
    /**
     * Update user profile.
     *
     * @param userId the user ID
     * @param request the update request
     * @return updated user profile
     * @throws NotFoundException if user not found
     */
    // PUBLIC_INTERFACE
    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UserProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if ("GOOGLE".equals(user.getAuthProvider())) {
                throw new BadRequestException("Cannot change password for OAuth2 users");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        
        return mapToProfileResponse(updatedUser);
    }
    
    /**
     * Delete user account.
     *
     * @param userId the user ID
     * @throws NotFoundException if user not found
     */
    // PUBLIC_INTERFACE
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }
    
    private UserProfileResponse mapToProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getAuthProvider(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
