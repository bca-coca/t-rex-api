package trex.com.web.achievement.controller;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trex.com.web.achievement.model.AchievementModel;
import trex.com.web.achievement.service.AchievementService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/achievements")
public class AchievementController {
    private final AchievementService service;

    @GetMapping("/{id}")
    @Cacheable(value = "achievement", key = "#id")
    public ResponseEntity<AchievementModel> getAchievement(@PathVariable Long id) {
        AchievementModel achievement = service.getAchievement(id);
        return ResponseEntity.ok(achievement);
    }

    @GetMapping
    @Cacheable(value = "achievements")
    public ResponseEntity<List<AchievementModel>> getAllAchievements() {
        List<AchievementModel> achievements = service.getAllAchievementsSortedByDate();
        return ResponseEntity.ok(achievements);
    }

    @PostMapping
    @CacheEvict(value = "achievements", allEntries = true)
    public ResponseEntity<AchievementModel> createAchievement(
        @Valid @RequestBody AchievementModel model
    ) {
        AchievementModel created = service.addAchievement(model);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "achievement", key = "#id")
    public ResponseEntity<AchievementModel> updateAchievement(
        @PathVariable Long id,
        @Valid @RequestBody AchievementModel model
    ) {
        AchievementModel updated = service.updateAchievement(id, model);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "achievement", key = "#id")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        service.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }
}