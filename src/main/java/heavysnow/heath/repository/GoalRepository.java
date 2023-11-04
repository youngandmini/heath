package heavysnow.heath.repository;

import heavysnow.heath.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
