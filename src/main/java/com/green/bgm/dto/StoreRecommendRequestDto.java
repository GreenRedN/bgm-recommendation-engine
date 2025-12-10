package com.green.bgm.dto;

import java.util.List;

public class StoreRecommendRequestDto {

    private String space;
    private String interior;
    private String weather;
    private String season;
    private String time;
    private String density;
    private String purpose;

    // 최근 재생한 곡 id 리스트
    private List<Long> recentHistory;

    public String getSpace() { return space; }
    public String getInterior() { return interior; }
    public String getWeather() { return weather; }
    public String getSeason() { return season; }
    public String getTime() { return time; }
    public String getDensity() { return density; }
    public String getPurpose() { return purpose; }
    public List<Long> getRecentHistory() { return recentHistory; }

    // ====== setter 추가 ======
    public void setSpace(String space) { this.space = space; }
    public void setInterior(String interior) { this.interior = interior; }
    public void setWeather(String weather) { this.weather = weather; }
    public void setSeason(String season) { this.season = season; }
    public void setTime(String time) { this.time = time; }
    public void setDensity(String density) { this.density = density; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setRecentHistory(List<Long> recentHistory) { this.recentHistory = recentHistory; }
}
