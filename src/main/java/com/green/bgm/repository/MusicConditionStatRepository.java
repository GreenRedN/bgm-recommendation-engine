package com.green.bgm.repository;

import com.green.bgm.domain.Music;
import com.green.bgm.domain.MusicConditionStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MusicConditionStatRepository extends JpaRepository<MusicConditionStat, Long> {

    Optional<MusicConditionStat> findByMusicAndConditionKey(Music music, String conditionKey);
}