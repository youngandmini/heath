package heavysnow.heath.repository;

import heavysnow.heath.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    @Query ("select distinct m from Member m left join fetch m.goals where m.id = :memberId")
    Optional<Member> findByIdWithGoals(@Param("memberId") Long memberId);
}
