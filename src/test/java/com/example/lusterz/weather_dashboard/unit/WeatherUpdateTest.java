package com.example.lusterz.weather_dashboard.unit;

import com.example.lusterz.weather_dashboard.model.Weather;
import com.example.lusterz.weather_dashboard.repository.WeatherRepository;
import com.example.lusterz.weather_dashboard.service.WeatherApiClient;
import com.example.lusterz.weather_dashboard.service.WeatherMapper;
import com.example.lusterz.weather_dashboard.service.WeatherUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherUpdateTest {

    @Mock
    private WeatherApiClient weatherApiClient;

    @Mock
    private WeatherMapper weatherMapper;

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherUpdate weatherUpdate;

    private Weather weather;

    @BeforeEach
    void setUp() {
        weather = new Weather();
        weather.setCityName("Cairo");
    }

    @Test
    void saveSingleCityWeather_shouldFetchMapAndSave_whenCityIsValid() {
        when(weatherApiClient.fetchWeatherJson("Cairo")).thenReturn(Mono.just("{\"city\":\"Cairo\"}"));
        when(weatherMapper.map(anyString())).thenReturn(weather);

        Weather result = weatherUpdate.saveSingleCityWeather("Cairo").block();

        assertEquals(weather, result);
        verify(weatherMapper).map("{\"city\":\"Cairo\"}");
        verify(weatherRepository).save(weather);
    }

    @Test
    void saveSingleCityWeather_shouldPropagateError_whenApiFails() {
        when(weatherApiClient.fetchWeatherJson("Cairo")).thenReturn(Mono.error(new RuntimeException("API unavailable")));

        Mono<Weather> result = weatherUpdate.saveSingleCityWeather("Cairo");

        assertThrows(RuntimeException.class, result::block);
        verify(weatherRepository, never()).save(any());
    }

    @Test
    void saveCitiesWeather_shouldSaveWeatherForAllCities() {
        when(weatherApiClient.fetchWeatherJson(anyString())).thenReturn(Mono.just("{}"));
        when(weatherMapper.map(anyString())).thenReturn(weather);

        List<Weather> result = weatherUpdate.saveCitiesWeather().block();

        assertEquals(20, result.size());
        verify(weatherRepository, times(20)).save(any(Weather.class));
    }


    @Test
    void deleteOldWeatherData_shouldDeleteWeatherOlderThan30Days() {
        weatherUpdate.deleteOldWeatherData();

        verify(weatherRepository).deleteByLastUpdatedBefore(argThat(cutoff ->
                cutoff.isBefore(LocalDateTime.now().minusDays(29)) &&
                        cutoff.isAfter(LocalDateTime.now().minusDays(31))
        ));
    }
}
