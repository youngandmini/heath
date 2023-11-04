package heavysnow.heath.service;

import heavysnow.heath.domain.Goal;
import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.GoalCreationDto;
import heavysnow.heath.repository.GoalRepository;
import heavysnow.heath.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;

    @Transactional
    // 특정 멤버에 대한 목표를 생성하는 메서드
    public Goal createGoalForMember(Long memberId, GoalCreationDto goalDto){
        // JPA save()메서드로 GoalCreationDto에 저장된 값을 Goal 데이터 베이스에 저장
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을수 없습니다." + memberId));

        Goal goal = Goal.builder()
                .member(member)
                .content(goalDto.getContent())
                .isAchieved(goalDto.isAchieved())
                .build();

        return goalRepository.save(goal);
    }

    // 조회 : 특정 멤버에 대한 모든 목표 조회하는 메서드
    public List<Goal> findGoalsByMember(Long memberId) {
        return goalRepository.findByMemberId(memberId);
    }
}
