package com.example.lusterz.weather_dashboard.service;

import com.example.lusterz.weather_dashboard.model.Weather;
import com.example.lusterz.weather_dashboard.repository.WeatherRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WeatherUpdate {

    private final WeatherApiClient weatherApiClient;
    private final WeatherMapper weatherMapper;
    private final WeatherRepository weatherRepository;

    private final List<String> cities = List.of(
            "London", "Paris", "Tokyo", "Beijing", "Moscow",
            "Dubai", "Singapore", "Sydney", "Berlin", "Rome",
            "Madrid", "Toronto", "Chicago", "Bangkok", "Seoul",
            "Cairo", "Mumbai", "Istanbul", "Osaka", "Rio"
    );

    public WeatherUpdate(WeatherApiClient weatherApiClient, WeatherMapper weatherMapper, WeatherRepository weatherRepository) {
        this.weatherApiClient = weatherApiClient;
        this.weatherMapper = weatherMapper;
        this.weatherRepository = weatherRepository;
    }

    public Mono<Weather> saveSingleCityWeather(String city) {
        return weatherApiClient.fetchWeatherJson(city)
                .map(weatherMapper::map)
                .doOnNext(weatherRepository::save);
    }

    public Mono<List<Weather>> saveCitiesWeather() {
        return Flux
                .fromIterable(cities)
                .flatMap(this::saveSingleCityWeather)
                .collectList();
    }

    // update weather data // update every  1 hour
    @Scheduled(fixedRate = 10 * 10 * 1000) 
    public void updateWeatherData() {
        saveCitiesWeather().subscribe();
    }

    // delete data older that 1 month // runs every day at midnight
    @Scheduled(cron = "0 0 0 * * *") 
    public void deleteOldWeatherData() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        weatherRepository.deleteByLastUpdatedBefore(cutoff);
    }
}
