package heavysnow.heath.service;

import heavysnow.heath.domain.Goal;
import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.goal.GoalCreateRequest;
import heavysnow.heath.dto.goal.GoalCreateResponse;
import heavysnow.heath.dto.goal.GoalUpdateRequest;
import heavysnow.heath.exception.ForbiddenException;
import heavysnow.heath.exception.NotFoundException;
import heavysnow.heath.repository.GoalRepository;
import heavysnow.heath.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;

    @Transactional
    // 특정 멤버에 대한 목표를 생성하는 메서드
    // 반환 타입 Long(goalId)로 변경
    public GoalCreateResponse createGoalForMember(Long loginMemberId, Long memberId, GoalCreateRequest goalDto){

        // JPA save()메서드로 GoalCreationDto에 저장된 값을 Goal 데이터 베이스에 저장
        Member member = memberRepository.findById(memberId).orElseThrow();

        if (!loginMemberId.equals(memberId)) {
            throw new ForbiddenException();
        }

        Goal goal = Goal.builder()
                .member(member)
                .content(goalDto.getContent())
                .isAchieved(goalDto.isAchieved())
                .build();

        Goal savedGoal = goalRepository.save(goal);
        return new GoalCreateResponse(savedGoal.getId());
    }

    // 수정 : 특정 멤버에 대한 목표 수정하는 메서드
    // 반환 타입 void로 변경
    @Transactional
    public void updateGoalForMember(Long loginMemberId, Long memberId, Long goalId, GoalUpdateRequest updateDto){
        // 목표 조회
        Goal goal = goalRepository.findByIdAndMemberId(goalId, memberId).orElseThrow(NotFoundException::new);

        if (!loginMemberId.equals(memberId)) {
            throw new ForbiddenException();
        }


        // 값 변경
        goal.update(updateDto.isAchieved());
    }


    // 삭제 : 특정 멤버에 대한 목표 삭제하는 메서드
    @Transactional
    public void deleteGoalForMember(Long loginMemberId, Long memberId, Long goalId){

        Member member = memberRepository.findById(memberId).orElseThrow();
        // 목표 조회
        Goal goal = goalRepository.findByIdAndMemberId(goalId, memberId).orElseThrow(NotFoundException::new);

        if (!loginMemberId.equals(memberId)) {
            throw new ForbiddenException();
        }

        goalRepository.delete(goal);
    }
}
