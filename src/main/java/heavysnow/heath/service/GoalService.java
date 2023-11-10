package heavysnow.heath.service;

import heavysnow.heath.domain.Goal;
import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.GoalCreationDto;
import heavysnow.heath.dto.GoalUpdateDto;
import heavysnow.heath.exception.UnauthorizedException;
import heavysnow.heath.repository.GoalRepository;
import heavysnow.heath.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
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
    // 반환 타입 Long(goalId)로 변경
    public Long createGoalForMember(Long memberId, GoalCreationDto goalDto){
        // JPA save()메서드로 GoalCreationDto에 저장된 값을 Goal 데이터 베이스에 저장
        Member member = memberRepository.findById(memberId).orElseThrow(UnauthorizedException::new);
//                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을수 없습니다." + memberId));

        Goal goal = Goal.builder()
                .member(member)
                .content(goalDto.getContent())
                .isAchieved(goalDto.isAchieved())
                .build();

        Goal savedGoal = goalRepository.save(goal);
        return savedGoal.getId();
    }

    // 수정 : 특정 멤버에 대한 목표 수정하는 메서드
    // 반환 타입 void로 변경
    @Transactional
    public void updateGoalForMember(Long memberId, Long goalId, GoalUpdateDto updateDto){
        // 목표 조회
        Goal goal = goalRepository.findByIdAndMemberId(goalId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버에 대한 목표를 찾을 수 업습니다."));

        // 값 변경
        goal.update(updateDto.getContent(), updateDto.isAchieved());
    }


    // 삭제 : 특정 멤버에 대한 목표 삭제하는 메서드
    @Transactional
    public void deleteGoalForMember(Long memberId, Long goalId){
        // 목표 조회
        Goal goal = goalRepository.findByIdAndMemberId(goalId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버에 대한 목표를 찾을 수 업습니다."));

        goalRepository.delete(goal);
    }
}
