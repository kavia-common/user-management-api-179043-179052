package com.example.usermanagementbackend.security;

import com.example.usermanagementbackend.model.User;
import com.example.usermanagementbackend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Handler for successful OAuth2 authentication.
 * Creates or updates user and generates JWT token.
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub");
        
        // Find or create user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFullName(name);
                    newUser.setAuthProvider("GOOGLE");
                    newUser.setProviderId(providerId);
                    newUser.setCreatedAt(LocalDateTime.now());
                    newUser.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(newUser);
                });
        
        // Update provider info if needed
        if (user.getAuthProvider() == null || !user.getAuthProvider().equals("GOOGLE")) {
            user.setAuthProvider("GOOGLE");
            user.setProviderId(providerId);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
        
        // Generate JWT token
        String token = tokenProvider.generateToken(email);
        
        // Redirect to frontend with token (or return JSON in real implementation)
        String redirectUrl = "/oauth2/redirect?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
