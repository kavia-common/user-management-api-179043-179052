package com.example.usermanagementbackend.service;

import com.example.usermanagementbackend.dto.AuthResponse;
import com.example.usermanagementbackend.dto.LoginRequest;
import com.example.usermanagementbackend.dto.RegisterRequest;
import com.example.usermanagementbackend.exception.BadRequestException;
import com.example.usermanagementbackend.model.User;
import com.example.usermanagementbackend.repository.UserRepository;
import com.example.usermanagementbackend.security.CustomUserDetails;
import com.example.usermanagementbackend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for handling user authentication operations.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    
    /**
     * Register a new user with email and password.
     *
     * @param request the registration request
     * @return authentication response with JWT token
     * @throws BadRequestException if email already exists
     */
    // PUBLIC_INTERFACE
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email address already in use");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setAuthProvider("LOCAL");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        
        String token = tokenProvider.generateToken(savedUser.getEmail());
        
        return new AuthResponse(token, savedUser.getId(), savedUser.getEmail(), savedUser.getFullName());
    }
    
    /**
     * Authenticate user with email and password.
     *
     * @param request the login request
     * @return authentication response with JWT token
     */
    // PUBLIC_INTERFACE
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = tokenProvider.generateToken(request.getEmail());
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName());
    }
    
    /**
     * Get currently authenticated user.
     *
     * @return the authenticated user
     */
    // PUBLIC_INTERFACE
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }
}
