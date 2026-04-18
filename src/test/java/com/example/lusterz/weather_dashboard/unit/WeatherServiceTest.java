package com.example.lusterz.weather_dashboard.unit;

import com.example.lusterz.weather_dashboard.model.Weather;
import com.example.lusterz.weather_dashboard.repository.WeatherRepository;
import com.example.lusterz.weather_dashboard.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherService weatherService;

    private Weather weather;

    @BeforeEach
    void setUp() {
        weather = new Weather();
        weather.setCityName("Cairo");
    }

    @Test
    void getSingleCityWeather_shouldReturnWeather_whenCityExists() {
        when(weatherRepository.findFirstByCityNameOrderByLastUpdatedDesc("Cairo")).thenReturn(Optional.of(weather));

        Optional<Weather> result = weatherService.getSingleCityWeather("Cairo");

        assertTrue(result.isPresent());
        assertEquals(weather, result.get());
    }

    @Test
    void getSingleCityWeather_shouldReturnEmpty_whenCityNotFound() {
        when(weatherRepository.findFirstByCityNameOrderByLastUpdatedDesc("Unknown")).thenReturn(Optional.empty());

        Optional<Weather> result = weatherService.getSingleCityWeather("Unknown");

        assertFalse(result.isPresent());
    }


    @Test
    void getCitiesWeather_shouldReturnAllCities() {
        when(weatherRepository.findAll()).thenReturn(List.of(weather));

        List<Weather> result = weatherService.getCitiesWeather();

        assertEquals(1, result.size());
        assertTrue(result.contains(weather));
    }

    @Test
    void getCitiesWeather_shouldReturnEmpty_whenNoCitiesExist() {
        when(weatherRepository.findAll()).thenReturn(Collections.emptyList());

        List<Weather> result = weatherService.getCitiesWeather();

        assertTrue(result.isEmpty());
    }
}
