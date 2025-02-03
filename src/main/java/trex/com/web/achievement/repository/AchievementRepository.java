package trex.com.web.achievement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trex.com.web.achievement.model.AchievementModel;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementModel, Long> {
}
