package com.example.lusterz.weather_dashboard.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lusterz.weather_dashboard.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
}
