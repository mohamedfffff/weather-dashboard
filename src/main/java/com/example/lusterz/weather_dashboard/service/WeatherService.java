package com.example.lusterz.weather_dashboard.service;

import com.example.lusterz.weather_dashboard.model.Weather;
import com.example.lusterz.weather_dashboard.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public Optional<Weather> getSingleCityWeather(String city) {
        return weatherRepository.findFirstByCityNameOrderByLastUpdatedDesc(city);
    }

    public List<Weather> getCitiesWeather() {
        return weatherRepository.findAll();
    }



}
