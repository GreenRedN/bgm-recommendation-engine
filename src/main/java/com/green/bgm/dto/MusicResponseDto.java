package com.green.bgm.dto;

public class MusicResponseDto {

    private Long id;
    private String title;
    private String youtubeId;

    // ★ 추가: 현재 조건
    private String weather;
    private String season;
    private String time;

    public MusicResponseDto(Long id,
                            String title,
                            String youtubeId,
                            String weather,
                            String season,
                            String time) {
        this.id = id;
        this.title = title;
        this.youtubeId = youtubeId;
        this.weather = weather;
        this.season = season;
        this.time = time;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getYoutubeId() { return youtubeId; }

    public String getWeather() { return weather; }
    public String getSeason() { return season; }
    public String getTime() { return time; }
}
