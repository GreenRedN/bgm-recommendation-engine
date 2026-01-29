package com.green.bgm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    @Value("${bgm.store.city:Seoul}")
    private String defaultCity;

    @Value("${bgm.weather.enabled:true}")
    private boolean enabled;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getCurrentWeatherTag(String city) {
        // 망 분리/키 미설정 같은 환경에서도 추천 흐름이 끊기지 않게 폴백
        if (!enabled || apiKey == null || apiKey.isBlank()) {
            return "cloudy";
        }

        if (city == null || city.isBlank()) {
            city = defaultCity;
        }

        String uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .build()
                .toUriString();

        // JSON 문자열 → Map 으로 바로 받기 (org.json 필요 없음)
        Map<String, Object> json;
        try {
            json = restTemplate.getForObject(uri, Map.class);
        } catch (RestClientException e) {
            return "cloudy";
        }
        if (json == null) {
            return "cloudy"; // 실패 시 기본값
        }

        Object weatherObj = json.get("weather");
        if (!(weatherObj instanceof List)) {
            return "cloudy";
        }

        List<?> weatherList = (List<?>) weatherObj;
        if (weatherList.isEmpty()) {
            return "cloudy";
        }

        Object first = weatherList.get(0);
        if (!(first instanceof Map)) {
            return "cloudy";
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> firstWeather = (Map<String, Object>) first;
        Object mainObj = firstWeather.get("main");
        if (!(mainObj instanceof String)) {
            return "cloudy";
        }

        String main = ((String) mainObj); // 예: Clear, Clouds, Rain, Snow ...

        return mapOpenWeatherToTag(main);
    }

    private String mapOpenWeatherToTag(String main) {
        String m = main.toLowerCase();
        if (m.contains("clear")) return "sunny";
        if (m.contains("rain") || m.contains("drizzle") || m.contains("thunderstorm")) return "rain";
        if (m.contains("snow")) return "snow";
        return "cloudy";
    }
}