package trex.com.web.achievement.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trex.com.web.achievement.model.AchievementModel;
import trex.com.web.achievement.repository.AchievementRepository;
import trex.com.web.exception.ResourceNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementService {
    private final AchievementRepository repository;

    @Cacheable(value = "achievements")
    public List<AchievementModel> getAllAchievementsSortedByDate() {
        try {
            log.info("Fetching all achievements sorted by date");
            return repository.findAll().stream()
            .sorted(Comparator.comparing(AchievementModel::getDate).reversed())
            .toList();
        } catch (Exception e) {
            log.error("Error fetching achievements: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch achievements", e);
        }
    }

    @Transactional
    @CachePut(value = "achievements", key = "#result.id")
    public AchievementModel addAchievement(AchievementModel model) {
        try {
            log.info("Attempting to add achievement: {}", model);
            AchievementModel savedAchievement = repository.save(model);
            log.info("Added achievement: {}", savedAchievement);
            return savedAchievement;
        } catch (Exception e) {
            log.error("Error adding achievement: {}", e.getMessage());
            throw new RuntimeException("Unable to add achievement", e);
        }
    }

    @Transactional
    @CachePut(value = "achievements", key = "#id")
    public AchievementModel updateAchievement(Long id, AchievementModel model) {
        try {
            log.info("Updating achievement with ID: {}", id);
            AchievementModel existingAchievement = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Achievement", "id", id));
            
            existingAchievement.setDate(model.getDate());
            existingAchievement.setCollege(model.getCollege());
            existingAchievement.setEvent(model.getEvent());
            existingAchievement.setPosition(model.getPosition());
            
            AchievementModel updatedAchievement = repository.save(existingAchievement);
            log.info("Updated achievement: {}", updatedAchievement);
            return updatedAchievement;
        } catch (ResourceNotFoundException e) {
            log.error("Achievement not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error updating achievement: {}", e.getMessage());
            throw new RuntimeException("Unable to update achievement", e);
        }
    }

    @Transactional
    @CacheEvict(value = "achievements", allEntries = true)
    public void deleteAchievement(Long id) {
        try {
            log.info("Deleting achievement with ID: {}", id);
            if (!repository.existsById(id)) {
                throw new ResourceNotFoundException("Achievement", "id", id);
            }
            repository.deleteById(id);
            log.info("Deleted achievement with ID: {}", id);
        } catch (ResourceNotFoundException e) {
            log.error("Achievement not found: {}", e.getMessage());
            throw e;
        } catch (EmptyResultDataAccessException e) {
            log.error("Error deleting achievement: {}", e.getMessage());
            throw new RuntimeException("Unable to delete achievement", e);
        }
    }

    @Cacheable(value = "achievement", key = "#id")
    public AchievementModel getAchievement(Long id) {
        try {
            log.info("Fetching achievement with ID: {}", id);
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Achievement", "id", id));
        } catch (ResourceNotFoundException e) {
            log.error("Achievement not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching achievement: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch achievement", e);
        }
    }
}