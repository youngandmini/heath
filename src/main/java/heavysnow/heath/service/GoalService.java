package heavysnow.heath.service;

import heavysnow.heath.domain.Goal;
import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.goal.GoalCreateRequest;
import heavysnow.heath.dto.goal.GoalCreateResponse;
import heavysnow.heath.dto.goal.GoalUpdateRequest;
import heavysnow.heath.exception.BadRequestException;
import heavysnow.heath.exception.ForbiddenException;
import heavysnow.heath.exception.NotFoundException;
import heavysnow.heath.repository.GoalRepository;
import heavysnow.heath.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class GoalService {
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;

    /**
     * 특정 멤버에 대한 목표를 생성하는 메서드
     * @param loginMemberId: 해당 멤버가 접속하여
     * @param memberId: 해당 멤버에
     * @param goalDto: 해당 내용으로 목표를 등록한다.
     * @return: 목표의 id 값을 반환
     */
    public GoalCreateResponse createGoalForMember(Long loginMemberId, Long memberId, GoalCreateRequest goalDto){

        // JPA save()메서드로 GoalCreationDto에 저장된 값을 Goal 데이터 베이스에 저장
        Member member = memberRepository.findById(memberId).orElseThrow();

        if (!loginMemberId.equals(memberId)) {
            throw new ForbiddenException();
        }

        List<Goal> memberGoals = goalRepository.findByMemberId(memberId);
        if (memberGoals.size() >= 5) {
            throw new BadRequestException();
        }

        Goal goal = Goal.builder()
                .member(member)
                .content(goalDto.getContent())
                .isAchieved(false)
                .build();

        Goal savedGoal = goalRepository.save(goal);
        return new GoalCreateResponse(savedGoal.getId());
    }

    /**
     * 특정 멤버에 대한 목표 수정하는 메서드
     * @param loginMemberId: 해당 멤버로 접속하여
     * @param memberId: 해당 멤버의
     * @param goalId: 해당 목표를
     * @param updateDto: 해당 내용으로 수정한다.
     */
    public void updateGoalForMember(Long loginMemberId, Long memberId, Long goalId, GoalUpdateRequest updateDto){
        // 목표 조회
        Goal goal = goalRepository.findByIdAndMemberId(goalId, memberId).orElseThrow(NotFoundException::new);

        if (!loginMemberId.equals(memberId)) {
            throw new ForbiddenException();
        }

        // 값 변경
        goal.update(updateDto.getIsAchieved());
    }

    /**
     * 특정 멤버에 대한 목표 삭제하는 메서드
     * @param loginMemberId: 해당 멤버가 접속하여
     * @param memberId: 해당 멤버의
     * @param goalId:해당 목표를 삭제한다.
     */
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
