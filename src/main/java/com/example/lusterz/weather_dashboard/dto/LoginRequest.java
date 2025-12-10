package com.example.lusterz.weather_dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class LoginRequest {
    
    private String name;
    private String password;
}
