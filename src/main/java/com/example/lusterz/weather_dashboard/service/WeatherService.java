package com.example.lusterz.weather_dashboard.service;

import com.example.lusterz.weather_dashboard.model.Weather;
import com.example.lusterz.weather_dashboard.repository.WeatherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Weather> getCitiesWeather(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return weatherRepository.findAll(pageable);
    }
}
