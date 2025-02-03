package trex.com.web.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import trex.com.web.team.model.TeamModel;


@Repository
public interface TeamRepository extends JpaRepository<TeamModel,Long> {
}
