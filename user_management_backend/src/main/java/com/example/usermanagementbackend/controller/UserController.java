package com.example.usermanagementbackend.controller;

import com.example.usermanagementbackend.dto.UserProfileRequest;
import com.example.usermanagementbackend.dto.UserProfileResponse;
import com.example.usermanagementbackend.model.User;
import com.example.usermanagementbackend.security.CustomUserDetails;
import com.example.usermanagementbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for user profile management.
 * Provides endpoints for viewing, updating, and deleting user profiles.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User profile management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Get user profile.
     *
     * @param userDetails the authenticated user details
     * @return user profile information
     */
    // PUBLIC_INTERFACE
    @GetMapping("/profile")
    @Operation(
        summary = "Get user profile",
        description = "Retrieve the profile of the currently authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse response = userService.getUserProfile(userDetails.getId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update user profile.
     *
     * @param userDetails the authenticated user details
     * @param request the update request containing new profile data
     * @return updated user profile
     */
    // PUBLIC_INTERFACE
    @PutMapping("/profile")
    @Operation(
        summary = "Update user profile",
        description = "Update the profile information of the currently authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserProfileRequest request) {
        UserProfileResponse response = userService.updateUserProfile(userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete user account.
     *
     * @param userDetails the authenticated user details
     * @return success message
     */
    // PUBLIC_INTERFACE
    @DeleteMapping("/profile")
    @Operation(
        summary = "Delete user account",
        description = "Permanently delete the currently authenticated user's account"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getId());
        return ResponseEntity.ok("User account deleted successfully");
    }
}
