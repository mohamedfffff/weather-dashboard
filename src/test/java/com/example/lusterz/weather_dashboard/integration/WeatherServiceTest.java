package com.example.lusterz.weather_dashboard.integration;

import com.example.lusterz.weather_dashboard.model.Weather;
import com.example.lusterz.weather_dashboard.repository.WeatherRepository;
import com.example.lusterz.weather_dashboard.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherRepository weatherRepository;

    @BeforeEach
    void setUp() {
        weatherRepository.deleteAll();
    }

    @Test
    void getSingleCityWeather_shouldReturnWeather_whenCityExists() {
        Weather weather = new Weather();
        weather.setCityName("Cairo");
        weatherRepository.save(weather);

        Optional<Weather> result = weatherService.getSingleCityWeather("Cairo");

        assertTrue(result.isPresent());
        assertEquals("Cairo", result.get().getCityName());
    }

    @Test
    void getSingleCityWeather_shouldReturnEmpty_whenCityNotFound() {
        Optional<Weather> result = weatherService.getSingleCityWeather("Unknown");

        assertFalse(result.isPresent());
    }

    @Test
    void getCitiesWeather_shouldReturnAllCities() {
        Weather weather = new Weather();
        weather.setCityName("Cairo");
        weatherRepository.save(weather);

        List<Weather> result = weatherService.getCitiesWeather();

        assertEquals(1, result.size());
    }

    @Test
    void getCitiesWeather_shouldReturnEmpty_whenNoCitiesExist() {
        List<Weather> result = weatherService.getCitiesWeather();

        assertTrue(result.isEmpty());
    }
}
