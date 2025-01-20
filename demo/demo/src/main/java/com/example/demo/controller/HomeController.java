package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Iterator;

@Controller
@ResponseBody
public class HomeController {

    @GetMapping("/")
    public String homePage(){
        return "home";
    }
    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // signup.html이 templates 디렉터리에 있어야 함
    }
}
