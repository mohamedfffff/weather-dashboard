package com.example.lusterz.weather_dashboard.unit;

import com.example.lusterz.weather_dashboard.dto.SignupRequest;
import com.example.lusterz.weather_dashboard.model.User;
import com.example.lusterz.weather_dashboard.repository.UserRepository;
import com.example.lusterz.weather_dashboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("encoded_password");

        signupRequest = new SignupRequest();
        signupRequest.setName("John Doe");
        signupRequest.setEmail("john@example.com");
        signupRequest.setPassword("raw_password");
    }


    @Test
    void getSingleUser_shouldReturnUser_whenUserExists() {
        when(userRepository.findByName("John Doe")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getSingleUser("John Doe");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void getSingleUser_shouldReturnEmpty_whenUserNotFound() {
        when(userRepository.findByName("Unknown")).thenReturn(Optional.empty());

        Optional<User> result = userService.getSingleUser("Unknown");

        assertFalse(result.isPresent());
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertTrue(result.contains(user));
    }

    @Test
    void getAllUsers_shouldReturnEmpty_whenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void registerNewUser_shouldSaveAndReturnUser() {
        when(passwordEncoder.encode("raw_password")).thenReturn("encoded_password");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.registerNewUser(signupRequest);

        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("encoded_password", result.getPassword());
        verify(passwordEncoder).encode("raw_password");
        verify(userRepository).save(any(User.class));
    }
}
