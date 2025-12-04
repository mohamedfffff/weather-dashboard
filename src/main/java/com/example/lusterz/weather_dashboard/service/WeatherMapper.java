package com.example.lusterz.weather_dashboard.service;

import com.example.lusterz.weather_dashboard.model.Weather;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Component
public class WeatherMapper {

    private final ObjectMapper mapper;

    // Inject ObjectMapper via constructor
    public WeatherMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Weather map(String json) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode location = root.get("location");
            JsonNode current = root.get("current");
            JsonNode condition = current.get("condition");

            Weather weather = new Weather();
            weather.setCityName(location.get("name").asString());
            weather.setCountry(location.get("country").asText());
            weather.setTempC(current.get("temp_c").asDouble());
            weather.setTempF(current.get("temp_f").asDouble());
            weather.setCondition(condition.get("text").asText());
            weather.setIcon(condition.get("icon").asText());
            weather.setHumidity(current.get("humidity").asInt());
            weather.setWindSpeed(current.get("wind_kph").asDouble());
            weather.setWindDirection(current.get("wind_dir").asText());
            weather.setFeelsLike(current.get("feelslike_c").asDouble());
            weather.setLastUpdated(LocalDateTime.now());

            return weather;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
