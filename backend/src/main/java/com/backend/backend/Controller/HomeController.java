package com.backend.backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController{

    @GetMapping("/")
    public String Home(){
        return "Welcome home";
    }
    
}