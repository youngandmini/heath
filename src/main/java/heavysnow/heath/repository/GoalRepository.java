package heavysnow.heath.repository;

import heavysnow.heath.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    /**
     * 특정 멤버의 특정 목표를 조회
     * @param goalId: 해당 목표를 조회
     * @param memberId: 해당 멤버의 목표인지 함께 확인
     * @return: 목표를 반환
     */
    // SELECT g FROM Goal g WHERE g.id = :id AND g.member.id = :memberId
    Optional<Goal> findByIdAndMemberId(Long goalId, Long memberId);

    /**
     * 툭정 멤버의 목표들을 조회
     * @param memberId: 해당 멤버의 목표들을 조회
     * @return: 목표 리스트를 반환
     */
    // SELECT g.* FROM goals g INNER JOIN members m ON g.member_id = m.id WHERE m.id = ?
    List<Goal> findByMemberId(Long memberId);
}
