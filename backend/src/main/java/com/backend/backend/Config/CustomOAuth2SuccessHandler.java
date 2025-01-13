package com.backend.backend.Config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.backend.backend.Model.User;
import com.backend.backend.Repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository; 
    private final JwtTokenProvider jwtTokenProvider; 

    public CustomOAuth2SuccessHandler(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Extract attributes from OAuth2User
        String googleId = (String) oAuth2User.getAttribute("sub");
        String email = (String) oAuth2User.getAttribute("email");
        String name = (String) oAuth2User.getAttribute("name");
        String picture = (String) oAuth2User.getAttribute("picture");

    
        User user = userRepository.findByGoogleId(googleId)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setGoogleId(googleId);
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setPicture(picture);
                return userRepository.save(newUser);
            });

        // Generate JWT token for the user
        String jwt = jwtTokenProvider.generateToken(user);

        // Create a cookie with the JWT token
        Cookie cookie = new Cookie("auth_token", jwt);
        cookie.setHttpOnly(true); // Make sure the cookie is only accessible by the server
        cookie.setSecure(request.isSecure()); // Set to true if using HTTPS
        cookie.setPath("/"); // Make the cookie available across the entire application
        cookie.setMaxAge(7 * 24 * 60 * 60); // Set cookie expiration (7 days)

        // Add the cookie to the response
        response.addCookie(cookie);

        // Optionally, log the cookie value for debugging purposes (remove in production)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cook : cookies) {
                if ("auth_token".equals(cook.getName())) {
                    System.out.println("Auth Token: " + cook.getValue());
                }
            }
        }

        // Redirect the user to the dashboard after successful login
        response.sendRedirect("http://localhost:5173/dashboard");
    }
}
