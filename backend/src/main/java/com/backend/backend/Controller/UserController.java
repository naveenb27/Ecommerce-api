package com.backend.backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.Config.JwtTokenProvider;
import com.backend.backend.Model.User;
import com.backend.backend.Repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public UserController(JwtTokenProvider jwtTokenProvider, UserRepository userRepository){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> user(HttpServletRequest request) {
        
        var token = extractTokenFromString(request);

        System.out.println("While get user : "+token);

        if(token == null || jwtTokenProvider.validateToken(token) == null){
            return ResponseEntity.status(401).body(Map.of("Error", "User is not authenticated"));
        }

        Claims claims = jwtTokenProvider.validateToken(token);

        String googleId = claims.getSubject();

        User user = userRepository.findByGoogleId(googleId).orElseThrow(() -> new RuntimeException("User not found"));

        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", user.getName());
        userInfo.put("email", user.getEmail());
        userInfo.put("picture", user.getPicture()); // Add more user details if needed

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();

        var cookie = ResponseCookie.from("auth_token", "")
            .maxAge(0)
            .httpOnly(true)
            .secure(false) 
            .path("/")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok("Logged out successfully");
    }

    private String extractTokenFromString(HttpServletRequest request){
        var cookies = request.getCookies();

        if(cookies != null){
            for(var cookie : cookies){
                if("auth_token".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
