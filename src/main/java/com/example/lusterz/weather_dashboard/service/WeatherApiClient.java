package com.example.lusterz.weather_dashboard.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WeatherApiClient {

    private final WebClient webClient;
    @Value("${weather.api.url}")
    private String weatherApiUrl;
    @Value("${weather.api.key}")
    private String weatherApiKey;

    public WeatherApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> fetchWeatherJson(String city) {
        String url = weatherApiUrl + "?key=" + weatherApiKey + "&q=" + city;
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }

}
