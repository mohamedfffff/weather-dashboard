package com.example.lusterz.weather_dashboard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lusterz.weather_dashboard.dto.SignupRequest;
import com.example.lusterz.weather_dashboard.model.User;
import com.example.lusterz.weather_dashboard.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
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
    
    @PostMapping
    public ResponseEntity<String> registerNewUser(@RequestBody SignupRequest request) {
        if (userService.getSingleUser(request.getName()).isPresent()) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("User Exists");
        }

        userService.registerNewUser(request);
        
        return ResponseEntity.ok("User created successfully");
    }

    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers()) ;
    }

    @GetMapping(params = "name")
    public ResponseEntity<User> getSingleUser(@RequestParam String name) {
        Optional<User> user = userService.getSingleUser(name);
        if (user.isPresent()) {
            ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }
    
}
