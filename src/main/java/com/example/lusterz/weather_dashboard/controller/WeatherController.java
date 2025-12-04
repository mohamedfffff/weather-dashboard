package com.example.lusterz.weather_dashboard.controller;

import com.example.lusterz.weather_dashboard.model.Weather;
import com.example.lusterz.weather_dashboard.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping()
    public ResponseEntity<List<Weather>> getCitiesWeather() {
        List<Weather> list = weatherService.getCitiesWeather();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/city")
    public ResponseEntity<Weather> getSingleCityWeather(@RequestParam String city) {
        Optional<Weather> weather = weatherService.getSingleCityWeather(city);
        if (weather.isPresent()){
            return ResponseEntity.ok(weather.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
