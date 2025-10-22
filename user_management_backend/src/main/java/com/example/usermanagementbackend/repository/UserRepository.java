package com.example.usermanagementbackend.repository;

import com.example.usermanagementbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity database operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by email address.
     *
     * @param email the user's email
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by auth provider and provider ID.
     *
     * @param authProvider the authentication provider (e.g., GOOGLE)
     * @param providerId the provider-specific user ID
     * @return Optional containing the user if found
     */
    Optional<User> findByAuthProviderAndProviderId(String authProvider, String providerId);
    
    /**
     * Check if a user exists with the given email.
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}
