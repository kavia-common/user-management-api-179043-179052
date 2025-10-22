package com.example.usermanagementbackend.controller;

import com.example.usermanagementbackend.dto.AuthResponse;
import com.example.usermanagementbackend.dto.LoginRequest;
import com.example.usermanagementbackend.dto.RegisterRequest;
import com.example.usermanagementbackend.dto.UserProfileResponse;
import com.example.usermanagementbackend.model.User;
import com.example.usermanagementbackend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication endpoints.
 * Handles user registration, login, and current user retrieval.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Register a new user account.
     *
     * @param request the registration request containing email, password, and full name
     * @return authentication response with JWT token
     */
    // PUBLIC_INTERFACE
    @PostMapping("/register")
    @Operation(
        summary = "Register new user",
        description = "Create a new user account with email and password"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Authenticate user and get JWT token.
     *
     * @param request the login request containing email and password
     * @return authentication response with JWT token
     */
    // PUBLIC_INTERFACE
    @PostMapping("/login")
    @Operation(
        summary = "Login user",
        description = "Authenticate user with email and password, returns JWT token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get current authenticated user information.
     *
     * @return current user profile
     */
    // PUBLIC_INTERFACE
    @GetMapping("/me")
    @Operation(
        summary = "Get current user",
        description = "Retrieve information about the currently authenticated user",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<UserProfileResponse> getCurrentUser() {
        User user = authService.getCurrentUser();
        UserProfileResponse response = new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getAuthProvider(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        return ResponseEntity.ok(response);
    }
    
    /**
     * Initiate Google OAuth2 authentication.
     * This endpoint redirects to Google's OAuth2 consent screen.
     *
     * @return redirect information
     */
    // PUBLIC_INTERFACE
    @GetMapping("/oauth2/google")
    @Operation(
        summary = "Initiate Google OAuth2 login",
        description = "Redirects to Google OAuth2 consent screen. After successful authentication, user will be redirected back with a JWT token."
    )
    @ApiResponse(responseCode = "302", description = "Redirect to Google OAuth2")
    public ResponseEntity<String> initiateGoogleLogin() {
        return ResponseEntity.ok("Redirect to /oauth2/authorization/google to initiate Google login");
    }
}
