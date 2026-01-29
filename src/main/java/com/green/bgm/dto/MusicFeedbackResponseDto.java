package com.green.bgm.dto;

import java.time.LocalDateTime;

/**
 * 학습(누적) 반영 결과를 프론트로 돌려주는 응답 DTO.
 */
public class MusicFeedbackResponseDto {

    private Long musicId;
    private int globalSelectionCount;

    private String conditionKey;
    private int conditionSelectionCount;
    private LocalDateTime lastSelectedAt;

    public MusicFeedbackResponseDto(Long musicId,
                                   int globalSelectionCount,
                                   String conditionKey,
                                   int conditionSelectionCount,
                                   LocalDateTime lastSelectedAt) {
        this.musicId = musicId;
        this.globalSelectionCount = globalSelectionCount;
        this.conditionKey = conditionKey;
        this.conditionSelectionCount = conditionSelectionCount;
        this.lastSelectedAt = lastSelectedAt;
    }

    public Long getMusicId() {
        return musicId;
    }

    public int getGlobalSelectionCount() {
        return globalSelectionCount;
    }

    public String getConditionKey() {
        return conditionKey;
    }

    public int getConditionSelectionCount() {
        return conditionSelectionCount;
    }

    public LocalDateTime getLastSelectedAt() {
        return lastSelectedAt;
    }
}
