package com.green.bgm.service;

import com.green.bgm.domain.Music;
import com.green.bgm.domain.MusicConditionStat;
import com.green.bgm.dto.MusicFeedbackRequestDto;
import com.green.bgm.dto.MusicFeedbackResponseDto;
import com.green.bgm.dto.MusicResponseDto;
import com.green.bgm.dto.StoreRecommendRequestDto;
import com.green.bgm.repository.MusicRepository;
import com.green.bgm.repository.MusicConditionStatRepository;
import com.green.bgm.util.ConditionKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class StoreRecommendService {

    private final MusicRepository musicRepository;
    private final MusicConditionStatRepository statRepository;

    public StoreRecommendService(MusicRepository musicRepository,
                                 MusicConditionStatRepository statRepository) {
        this.musicRepository = musicRepository;
        this.statRepository = statRepository;
    }

    // ★ 핵심 추천 메소드: 상위 1곡만 반환 ★
    public MusicResponseDto recommend(StoreRecommendRequestDto req) {

        String space    = req.getSpace();
        String interior = req.getInterior();
        String weather  = req.getWeather();   // 컨트롤러에서 자동 세팅
        String season   = req.getSeason();    // 컨트롤러에서 자동 세팅
        String time     = req.getTime();      // 컨트롤러에서 자동 세팅
        String density  = req.getDensity();
        String purpose  = req.getPurpose();

        List<Long> recentHistory =
                (req.getRecentHistory() == null) ? Collections.emptyList() : req.getRecentHistory();

        // 1) 기본 필터링
        List<Music> candidates = filterMusic(space);
        if (candidates.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "조건에 맞는 후보 음악이 없습니다. (space 태그/active/playable 필터 결과 0건)"
            );
        }

        // 2) 조건키 생성 (interior/weather/time/density/purpose)
        String conditionKey = ConditionKeyGenerator.generate(req);

        // 3) 점수 계산
        Map<Long, Integer> scoreMap = new HashMap<>();

        for (Music m : candidates) {
            int score = 0;

            // baseScore
            score += m.getBaseScore();

            // (1) 조건 가중치
            score += calcConditionScore(m, interior, weather, season, time, density, purpose);

            // (2) 학습 가중치 (조건별 인기도 + 미선택 패널티)
            score += calcLearnedScore(m, conditionKey);

            // (3) 전역 인기도 (selectionCount × 1)
            score += m.getSelectionCount();

            // (4) 신곡 가산점
            score += calcNoveltyScore(m);

            // (5) 다양성 패널티
            score -= calcDiversityPenalty(m, recentHistory);

            scoreMap.put(m.getId(), score);
        }

        // 4) 점수 높은 곡 1개 선택
        Music best = null;
        int bestScore = Integer.MIN_VALUE;
        for (Music m : candidates) {
            int s = scoreMap.getOrDefault(m.getId(), 0);
            if (best == null || s > bestScore) {
                best = m;
                bestScore = s;
            }
        }

        // 5) DTO로 변환 (조건키도 함께 전달)
        return new MusicResponseDto(
                best.getId(),
                best.getTitle(),
                best.getYoutubeId(),
                weather,   // 컨트롤러에서 자동 세팅된 값
                season,
                time,
                conditionKey
        );
    }

    // ★ 사용자가 "선택"한 곡을 누적(학습)시키는 메소드 ★
    @Transactional
    public MusicFeedbackResponseDto recordSelection(MusicFeedbackRequestDto req) {

        if (req == null || req.getMusicId() == null) {
            throw new IllegalArgumentException("musicId는 필수입니다.");
        }

        Music music = musicRepository.findById(req.getMusicId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 musicId: " + req.getMusicId()));

        // 조건키 생성 (추천에서 사용한 조건을 그대로 보내는 방식)
        StoreRecommendRequestDto tmp = new StoreRecommendRequestDto();
        tmp.setInterior(req.getInterior());
        tmp.setWeather(req.getWeather());
        tmp.setSeason(req.getSeason());
        tmp.setTime(req.getTime());
        tmp.setDensity(req.getDensity());
        tmp.setPurpose(req.getPurpose());

        String conditionKey = ConditionKeyGenerator.generate(tmp);

        // 1) 전역 인기도 누적
        music.increaseSelectionCount();
        musicRepository.save(music);

        // 2) 조건별 누적 (없으면 생성)
        MusicConditionStat stat = statRepository.findByMusicAndConditionKey(music, conditionKey)
                .orElseGet(() -> new MusicConditionStat(music, conditionKey));

        stat.increaseSelectionCount();
        statRepository.save(stat);

        return new MusicFeedbackResponseDto(
                music.getId(),
                music.getSelectionCount(),
                conditionKey,
                stat.getSelectionCount(),
                stat.getLastSelectedAt()
        );
    }

    // ========== 1단계: 필터링 ==========
    private List<Music> filterMusic(String space) {
        // active=true, playable=true 만 우선 조회
        List<Music> baseList = musicRepository.findByActiveTrueAndPlayableTrue();

        // space가 비어있으면 그대로 반환
        if (space == null || space.isBlank()) {
            return baseList;
        }

        List<Music> result = new ArrayList<>();
        for (Music m : baseList) {
            String tags = m.getTags();
            if (tags != null && tags.contains(space)) {
                result.add(m);
            }
        }
        return result;
    }

    // ========== 2단계: 조건 가중치 ==========
    private int calcConditionScore(Music m,
                                   String interior, String weather, String season, String time,
                                   String density, String purpose) {

        String tags = m.getTags();
        if (tags == null) return 0;

        int score = 0;

        if (interior != null && tags.contains(interior)) score += 50; // 인테리어
        if (weather  != null && tags.contains(weather))  score += 30; // 날씨
        if (season   != null && tags.contains(season))   score += 10; // 계절
        if (time     != null && tags.contains(time))     score += 10; // 시간대
        if (density  != null && tags.contains(density))  score += 20; // 사람 밀도
        if (purpose  != null && tags.contains(purpose))  score += 20; // 활동 목적

        // 분위기/에너지 (간단 버전 – 필요하면 확장)
        if (tags.contains("calm") || tags.contains("chill")) {
            score += 25;
        }

        return score;
    }

    // ========== 3단계: 학습 가중치 ==========
    private int calcLearnedScore(Music music, String conditionKey) {

        // MusicConditionStatRepository 시그니처:
        // Optional<MusicConditionStat> findByMusicAndConditionKey(Music music, String conditionKey);
        Optional<MusicConditionStat> opt =
                statRepository.findByMusicAndConditionKey(music, conditionKey);

        if (opt.isEmpty()) {
            return 0;
        }

        MusicConditionStat stat = opt.get();
        LocalDateTime last = stat.getLastSelectedAt();
        if (last == null) {
            // 마지막 선택 시간이 없으면 단순 선택 횟수만 반영
            return stat.getSelectionCount();
        }

        long days = ChronoUnit.DAYS.between(last, LocalDateTime.now());
        int score = 0;

        // 최근 선택일수에 따른 가중치
        if (days <= 7) {
            score += stat.getSelectionCount() * 3;
        } else if (days <= 30) {
            score += stat.getSelectionCount() * 2;
        } else {
            score += stat.getSelectionCount();
        }

        // 미선택 패널티
        if (days >= 30) score -= 15;
        if (days >= 60) score -= 30;

        return score;
    }

    // ========== 4단계: 신곡 가산점 ==========
    private int calcNoveltyScore(Music m) {
        LocalDateTime createdAt = m.getCreatedAt();
        if (createdAt == null) return 0;

        long days = ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());
        if (days <= 7)  return 30;
        if (days <= 30) return 10;
        return 0;
    }

    // ========== 5단계: 다양성 패널티 ==========
    private int calcDiversityPenalty(Music m, List<Long> recent) {
        if (recent == null || recent.isEmpty()) return 0;

        Long id = m.getId();

        // 직전 곡이면 강한 패널티
        if (recent.get(0).equals(id)) {
            return 50;
        }

        // 최근 목록에 있으면 약한 패널티
        if (recent.contains(id)) {
            return 20;
        }

        return 0;
    }
}
