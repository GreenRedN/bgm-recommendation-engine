package com.green.bgm.dto;

public class MusicResponseDto {

    private Long id;
    private String title;
    private String youtubeId;

    // ★ 추가: 현재 조건
    private String weather;
    private String season;
    private String time;

    // ★ 추가: 이번 추천에 사용된 조건키(학습/검증용)
    private String conditionKey;

    public MusicResponseDto(Long id,
                            String title,
                            String youtubeId,
                            String weather,
                            String season,
                            String time,
                            String conditionKey) {
        this.id = id;
        this.title = title;
        this.youtubeId = youtubeId;
        this.weather = weather;
        this.season = season;
        this.time = time;
        this.conditionKey = conditionKey;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getYoutubeId() { return youtubeId; }

    public String getWeather() { return weather; }
    public String getSeason() { return season; }
    public String getTime() { return time; }

    public String getConditionKey() { return conditionKey; }
}
