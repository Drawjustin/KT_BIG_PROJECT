package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
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
