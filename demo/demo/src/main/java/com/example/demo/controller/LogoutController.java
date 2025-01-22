package com.example.demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LogoutController {

    @PostMapping("/logout")
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
