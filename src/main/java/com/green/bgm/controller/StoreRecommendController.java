package com.green.bgm.controller;

import com.green.bgm.dto.MusicFeedbackRequestDto;
import com.green.bgm.dto.MusicFeedbackResponseDto;
import com.green.bgm.dto.MusicResponseDto;
import com.green.bgm.dto.StoreRecommendRequestDto;
import com.green.bgm.service.StoreRecommendService;
import com.green.bgm.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/store")
public class StoreRecommendController {

    private final StoreRecommendService storeRecommendService;
    private final WeatherService weatherService;

    public StoreRecommendController(StoreRecommendService storeRecommendService,
                                    WeatherService weatherService) {
        this.storeRecommendService = storeRecommendService;
        this.weatherService = weatherService;
    }

    @PostMapping("/recommend")
    public MusicResponseDto recommend(@RequestBody StoreRecommendRequestDto requestDto) {

        applyAutoDetectedFields(requestDto);

        return storeRecommendService.recommend(requestDto);
    }

    // 사용자가 실제로 "선택"한 곡을 누적(학습)시키는 엔드포인트
    @PostMapping("/feedback")
    public MusicFeedbackResponseDto feedback(@RequestBody MusicFeedbackRequestDto requestDto) {

        applyAutoDetectedFields(requestDto);
        return storeRecommendService.recordSelection(requestDto);
    }

    // ====== 자동 감지 로직을 한 곳에 모아 재사용 ======
    private void applyAutoDetectedFields(StoreRecommendRequestDto requestDto) {

        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int hour = now.getHour();

        // 계절 자동 감지
        if (requestDto.getSeason() == null || requestDto.getSeason().isBlank()) {
            String season =
                    (month == 12 || month <= 2) ? "winter" :
                    (month <= 5) ? "spring" :
                    (month <= 8) ? "summer" : "autumn";
            requestDto.setSeason(season);
        }

        // 시간대 자동 감지
        if (requestDto.getTime() == null || requestDto.getTime().isBlank()) {
            String time =
                    (hour >= 6 && hour < 12) ? "morning" :
                    (hour < 18) ? "afternoon" :
                    (hour < 23) ? "evening" : "night";
            requestDto.setTime(time);
        }

        // 날씨 자동 감지 (값 안 넘어오면 OpenWeather → sunny/rain/cloudy/snow)
        if (requestDto.getWeather() == null || requestDto.getWeather().isBlank()) {
            String weatherTag = weatherService.getCurrentWeatherTag(null); // 기본 도시 Seoul
            requestDto.setWeather(weatherTag);
        }
    }
}
