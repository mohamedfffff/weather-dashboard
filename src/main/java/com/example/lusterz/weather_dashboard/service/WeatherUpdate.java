package com.example.lusterz.weather_dashboard.service;

import com.example.lusterz.weather_dashboard.model.Weather;
import com.example.lusterz.weather_dashboard.repository.WeatherRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class WeatherUpdate {

    private final WeatherApiClient weatherApiClient;
    private final WeatherMapper weatherMapper;
    private final WeatherRepository weatherRepository;
    private List<String> cities;

    // reading cities from static json list
    @PostConstruct
    public void loadCities() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("data/cities.json");
        // java deletes generic type at runtime, so a subclass is created to preserve it
        cities = mapper.readValue(resource.getInputStream(), new TypeReference<List<String>>() {});
    }

    public WeatherUpdate(WeatherApiClient weatherApiClient, WeatherMapper weatherMapper, WeatherRepository weatherRepository) {
        this.weatherApiClient = weatherApiClient;
        this.weatherMapper = weatherMapper;
        this.weatherRepository = weatherRepository;
    }

    // delete old data and save new one
    public Mono<Weather> saveSingleCityWeather(String city) {
        return weatherApiClient.fetchWeatherJson(city)
                .map(weatherMapper::map)
                .doOnNext(newData -> {
                        weatherRepository.deleteByCityName(newData.getCityName());
                        weatherRepository.save(newData);
                        }
                );
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
}
