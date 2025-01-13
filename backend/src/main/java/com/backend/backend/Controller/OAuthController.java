package com.backend.backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/oauth2/code/google")
public class OAuthController {
    @GetMapping
    public String handleGoogleCallback(@RequestParam String code) {
        System.out.println("Authorization Code: " + code);
        return "Login Successful!";
    }
}
