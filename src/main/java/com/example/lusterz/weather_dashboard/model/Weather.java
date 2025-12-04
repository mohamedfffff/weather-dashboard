package com.example.lusterz.weather_dashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cityName;
    private String country;
    private double tempC;
    private double tempF;
    private String condition;
    private String icon;
    private int humidity;
    private double windSpeed;
    private String windDirection;
    private double feelsLike;
    private LocalDateTime lastUpdated;

}

