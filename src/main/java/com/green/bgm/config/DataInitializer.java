package com.green.bgm.config;

import com.green.bgm.domain.Music;
import com.green.bgm.repository.MusicRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DataInitializer {

    private final MusicRepository musicRepository;
    private final Random random = new Random();

    public DataInitializer(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @PostConstruct
    public void init() {
        if (musicRepository.count() > 0) return;

        List<String> spaces = List.of("cafe", "bar", "fitness", "lounge", "office");
        List<String> interiors = List.of("modern", "wood", "industrial", "luxury", "minimal");
        List<String> weathers = List.of("sunny", "rain", "cloudy", "snow");
        List<String> times = List.of("morning", "afternoon", "evening", "night");
        List<String> moods = List.of("calm", "chill", "happy", "emotional", "energy");
        List<String> genres = List.of("lofi", "jazz", "piano", "edm", "acoustic");

        for (int i = 1; i <= 40; i++) {
            String tags = randomTags(spaces, interiors, weathers, times, moods, genres);

            String title = "Dummy Track " + i;
            String youtubeId = randomYouTubeId();
            int baseScore = random.nextInt(60) + 20;

            musicRepository.save(new Music(
                    title,
                    youtubeId,
                    tags,
                    baseScore
            ));
        }

        System.out.println("=== 40개 Music 더미 데이터 자동 생성됨 ===");
    }

    private String randomTags(List<String>... groups) {
        StringBuilder sb = new StringBuilder();
        for (List<String> group : groups) {
            sb.append(group.get(random.nextInt(group.size()))).append(",");
        }
        return sb.toString().replaceAll(",$", "");
    }

    private String randomYouTubeId() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }
}