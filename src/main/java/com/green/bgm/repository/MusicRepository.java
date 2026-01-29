package com.green.bgm.repository;

import com.green.bgm.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {

    List<Music> findByActiveTrueAndPlayableTrue();
}