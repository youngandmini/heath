package heavysnow.heath.repository;

import heavysnow.heath.domain.Goal;
import heavysnow.heath.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    // SELECT g FROM Goal g WHERE g.id = :id AND g.member.id = :memberId
    Optional<Goal> findByIdAndMemberId(Long goalId, Long memberId);

    // SELECT g.* FROM goals g INNER JOIN members m ON g.member_id = m.id WHERE m.id = ?
    List<Goal> findByMemberId(Long memberId);
}
