package heavysnow.heath.repository;

import heavysnow.heath.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 특정 멤버를 username(이메일)로 조회
     * @param username: 해당 이메일로 조회
     * @return: 회원을 반환
     */
    Optional<Member> findByUsername(String username);

    /**
     * 특정 회원을 목표와 함께 조회
     * @param memberId: 해당 회원을 목표와 함꼐 조회
     * @return: 회원을 반환 (목표와 함께)
     */
    @Query ("select distinct m from Member m left join fetch m.goals where m.id = :memberId")
    Optional<Member> findByIdWithGoals(@Param("memberId") Long memberId);
}
