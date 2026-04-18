package com.example.lusterz.weather_dashboard.integration;

import com.example.lusterz.weather_dashboard.model.Weather;
import com.example.lusterz.weather_dashboard.repository.WeatherRepository;
import com.example.lusterz.weather_dashboard.service.WeatherApiClient;
import com.example.lusterz.weather_dashboard.service.WeatherMapper;
import com.example.lusterz.weather_dashboard.service.WeatherUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class WeatherUpdateTest {

    // it is not advised to use real external api
    @MockitoBean
    private WeatherApiClient weatherApiClient;

    @Autowired
    private WeatherUpdate weatherUpdate;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private WeatherMapper weatherMapper;

    private static final String VALID_WEATHER_JSON = """
        {
            "location": {
                "name": "Cairo",
                "country": "Egypt"
            },
            "current": {
                "temp_c": 30.0,
                "temp_f": 86.0,
                "humidity": 50,
                "wind_kph": 15.0,
                "wind_dir": "NW",
                "feelslike_c": 32.0,
                "condition": {
                    "text": "Sunny",
                    "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png"
                }
            }
        }
        """;

    @BeforeEach
    void setUp() {
        weatherRepository.deleteAll();
    }

    // ── saveSingleCityWeather ───────────────────────────────────────────────

    @Test
    void saveSingleCityWeather_shouldSaveWeatherToDatabase() {
        when(weatherApiClient.fetchWeatherJson("Cairo")).thenReturn(Mono.just(VALID_WEATHER_JSON));

        weatherUpdate.saveSingleCityWeather("Cairo").block();

        Optional<Weather> saved = weatherRepository.findFirstByCityNameOrderByLastUpdatedDesc("Cairo");
        assertTrue(saved.isPresent());
        assertEquals("Cairo", saved.get().getCityName());
    }

    @Test
    void saveSingleCityWeather_shouldNotSave_whenApiFails() {
        when(weatherApiClient.fetchWeatherJson("Cairo")).thenReturn(Mono.error(new RuntimeException("API unavailable")));

        assertThrows(RuntimeException.class, () -> weatherUpdate.saveSingleCityWeather("Cairo").block());

        assertTrue(weatherRepository.findFirstByCityNameOrderByLastUpdatedDesc("Cairo").isEmpty());
    }

    @Test
    void saveCitiesWeather_shouldSaveAllCitiesToDatabase() {
        when(weatherApiClient.fetchWeatherJson(anyString())).thenReturn(Mono.just(VALID_WEATHER_JSON));

        weatherUpdate.saveCitiesWeather().block();

        assertEquals(20, weatherRepository.findAll().size());
    }

    @Test
    void deleteOldWeatherData_shouldDeleteWeatherOlderThan30Days() {
        Weather oldWeather = new Weather();
        oldWeather.setCityName("Cairo");
        oldWeather.setLastUpdated(LocalDateTime.now().minusDays(31));
        weatherRepository.save(oldWeather);

        weatherUpdate.deleteOldWeatherData();

        assertTrue(weatherRepository.findAll().isEmpty());
    }

    @Test
    void deleteOldWeatherData_shouldNotDeleteRecentWeather() {
        Weather recentWeather = new Weather();
        recentWeather.setCityName("Cairo");
        recentWeather.setLastUpdated(LocalDateTime.now().minusDays(1));
        weatherRepository.save(recentWeather);

        weatherUpdate.deleteOldWeatherData();

        assertEquals(1, weatherRepository.findAll().size());
    }
}
