package com.example.lusterz.weather_dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String Home(){
        return "index.html";
    }

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup.html";
    }

    @GetMapping("/logout")
    public String logout(){
        return "logout.html";
    }
}
