package com.example.demo.utils;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authentication information.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().getUserEmail();
    }

    public static String getCurrentUserRole() {
        return getCurrentUser().getUserRole();
    }
}