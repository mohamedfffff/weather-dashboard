package com.example.lusterz.weather_dashboard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lusterz.weather_dashboard.model.User;
import com.example.lusterz.weather_dashboard.service.UserService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public record SignupRequest(String name, String email, String password){}
    
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody SignupRequest request) {
        if (userService.getSingleUser(request.name).isPresent()) {
            return ResponseEntity.badRequest().body("User Taken");
        }

        User user = new User();
        user.setName(request.name);
        user.setEmail(request.email);
        user.setPassword(userService.encodePassword(request.password));

        userService.saveUser(user);
        
        return ResponseEntity.ok("User created successfully");
    }

    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers()) ;
    }
    
}
