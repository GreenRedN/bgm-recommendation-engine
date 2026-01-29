package com.green.bgm.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String youtubeId;

    // 예: "cafe,modern,sunny,morning,calm,lofi"
    @Column(length = 1000)
    private String tags;

    private int baseScore;

    private int selectionCount;

    private boolean active;

    private boolean playable;

    private LocalDateTime createdAt;

    protected Music() {
    }

    public Music(String title, String youtubeId, String tags, int baseScore) {
        this.title = title;
        this.youtubeId = youtubeId;
        this.tags = tags;
        this.baseScore = baseScore;
        this.active = true;
        this.playable = true;
        this.createdAt = LocalDateTime.now();
    }

    public void increaseSelectionCount() {
        this.selectionCount++;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getYoutubeId() { return youtubeId; }
    public String getTags() { return tags; }
    public int getBaseScore() { return baseScore; }
    public int getSelectionCount() { return selectionCount; }
    public boolean isActive() { return active; }
    public boolean isPlayable() { return playable; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}