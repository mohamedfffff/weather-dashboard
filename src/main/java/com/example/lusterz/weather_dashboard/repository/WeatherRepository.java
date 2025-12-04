package com.example.lusterz.weather_dashboard.repository;

import com.example.lusterz.weather_dashboard.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    // get data for a full month
    Optional<Weather> findByCityName(String  cityName);

    // get only latest data
    Optional<Weather> findFirstByCityNameOrderByLastUpdatedDesc(String  cityName);

    void deleteByLastUpdatedBefore(LocalDateTime date);

}
