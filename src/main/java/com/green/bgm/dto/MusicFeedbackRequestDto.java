package com.green.bgm.dto;

/**
 * 사용자가 추천된 곡을 실제로 "선택"했을 때, 학습(누적)을 위해 보내는 요청 DTO.
 *
 * - StoreRecommendRequestDto를 상속하여 (weather/season/time 자동 감지 로직을 재사용)
 * - musicId만 추가
 */
public class MusicFeedbackRequestDto extends StoreRecommendRequestDto {

    private Long musicId;

    public Long getMusicId() {
        return musicId;
    }

    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }
}
