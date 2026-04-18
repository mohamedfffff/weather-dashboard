package com.example.lusterz.weather_dashboard.integration;

import com.example.lusterz.weather_dashboard.dto.SignupRequest;
import com.example.lusterz.weather_dashboard.model.User;
import com.example.lusterz.weather_dashboard.repository.UserRepository;
import com.example.lusterz.weather_dashboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void getSingleUser_shouldReturnUser_whenUserExists() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("encoded_password");
        userRepository.save(user);

        Optional<User> result = userService.getSingleUser("John Doe");

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void getSingleUser_shouldReturnEmpty_whenUserNotFound() {
        Optional<User> result = userService.getSingleUser("Unknown");

        assertFalse(result.isPresent());
    }


    @Test
    void getAllUsers_shouldReturnAllUsers() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("encoded_password");
        userRepository.save(user);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
    }

    @Test
    void getAllUsers_shouldReturnEmpty_whenNoUsersExist() {
        List<User> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void registerNewUser_shouldPersistUserWithEncodedPassword() {
        SignupRequest request = new SignupRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("raw_password");

        userService.registerNewUser(request);

        Optional<User> saved = userRepository.findByName("John Doe");
        assertTrue(saved.isPresent());
        assertNotEquals("raw_password", saved.get().getPassword()); // confirms encoding happened
    }
}
