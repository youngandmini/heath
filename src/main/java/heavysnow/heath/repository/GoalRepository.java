package heavysnow.heath.repository;

import heavysnow.heath.domain.Goal;
import heavysnow.heath.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    // SELECT g.* FROM goals g INNER JOIN members m ON g.member_id = m.id WHERE m.id = ?
    List<Goal> findByMemberId(Long memberId);
}
