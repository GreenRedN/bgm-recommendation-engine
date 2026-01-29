package com.green.bgm.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MusicConditionStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Music music;

    // 예: "interior=modern|weather=rain|time=evening|density=high|purpose=talk"
    @Column(length = 500)
    private String conditionKey;

    private int selectionCount;

    private LocalDateTime lastSelectedAt;

    protected MusicConditionStat() {
    }

    public MusicConditionStat(Music music, String conditionKey) {
        this.music = music;
        this.conditionKey = conditionKey;
        this.selectionCount = 0;
        this.lastSelectedAt = LocalDateTime.now();
    }

    public void increaseSelectionCount() {
        this.selectionCount++;
        this.lastSelectedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Music getMusic() { return music; }
    public String getConditionKey() { return conditionKey; }
    public int getSelectionCount() { return selectionCount; }
    public LocalDateTime getLastSelectedAt() { return lastSelectedAt; }
}