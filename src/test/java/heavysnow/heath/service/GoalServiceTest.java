package heavysnow.heath.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import heavysnow.heath.domain.Goal;
import heavysnow.heath.dto.goal.GoalCreationDto;
import heavysnow.heath.dto.goal.GoalUpdateDto;
import heavysnow.heath.dto.member.MemberDto;
import heavysnow.heath.repository.GoalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트
class GoalServiceTest {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private GoalService goalService;

    @Autowired
    MemberService memberService;

    @Autowired
    GoalRepository goalRepository;

    @DisplayName("addGoal : 특정 멤버에 대한 목표 생성에 성공한다.")
    @Test
    public void addGoal() throws Exception {
        // given

        // 멤버 DTO
        MemberDto memberDto = MemberDto.builder()
                .username("leejungbin")
                .nickname("ego2")
                .userStatusMessage("fighting!!!")
                .profileImgUrl("None")
                .build();

        // memberDto에 있는 정보 데베에 저장
        Long savedMemberId = memberService.createUser(memberDto);


        final String content = "content";
        final boolean isAchieved = false;

        // Goal DTO
        GoalCreationDto goalCreationDto = new GoalCreationDto(content, isAchieved);

        // when
        // 해당 멤버의 목표 데베 저장
        Long savedGoalId = goalService.createGoalForMember(savedMemberId, savedMemberId, goalCreationDto).getGoalId();
        Goal findGoal = goalRepository.findById(savedGoalId).get();

        // then
        assertThat(findGoal.getMember().getId()).isEqualTo(savedMemberId);  // 같은 멤버인지 검증
        assertThat(findGoal.getContent()).isEqualTo(goalCreationDto.getContent());
        assertThat(findGoal.isAchieved()).isEqualTo(goalCreationDto.isAchieved());
    }

    @DisplayName("updateGoalForMember : 특정 멤버에 대한 목표의 달성 상태를 수정한다.")
    @Test
    @Transactional
    public void updateGoal() throws Exception {
        // given
        // 멤버 DTO
        MemberDto memberDto = MemberDto.builder()
                .username("leejungbin")
                .nickname("ego2")
                .userStatusMessage("fighting!!!")
                .profileImgUrl("None")
                .build();

        // memberDto에 있는 정보 데베에 저장
        Long savedMemberId = memberService.createUser(memberDto);


        // Goal DTO
        GoalCreationDto goalCreationDto1 = new GoalCreationDto("content1", false);
        GoalCreationDto goalCreationDto2 = new GoalCreationDto("content2", true);


        // 해당 멤버의 목표 데베 저장
        Long savedGoalId1 = goalService.createGoalForMember(savedMemberId, savedMemberId, goalCreationDto1).getGoalId();
        Long savedGoalId2 = goalService.createGoalForMember(savedMemberId, savedMemberId, goalCreationDto2).getGoalId();

        Boolean isAchieved = true;

        GoalUpdateDto updateDto = new GoalUpdateDto(isAchieved);


        // when
        goalService.updateGoalForMember(savedMemberId, savedMemberId, savedGoalId1, updateDto);
        Goal findGoal1 = goalRepository.findById(savedGoalId1).get();

        // then
        assertThat(findGoal1.isAchieved()).isEqualTo(isAchieved);

    }

    @DisplayName("deleteGoalForMember : 특정 멤버에 대한 목표를 삭제한다. ")
    @Test
    @Transactional
    public void deleteGoal() throws Exception {
        // given
        // 멤버 DTO
        MemberDto memberDto = MemberDto.builder()
                .username("leejungbin")
                .nickname("ego2")
                .userStatusMessage("fighting!!!")
                .profileImgUrl("None")
                .build();

        // memberDto에 있는 정보 데베에 저장
        Long savedMemberId = memberService.createUser(memberDto);

        // Goal DTO
        GoalCreationDto goalCreationDto = new GoalCreationDto("1일 1커밋 하기", false);

        // 멤버의 목표 데베에 저장
        Long savedGoalId = goalService.createGoalForMember(savedMemberId, savedMemberId, goalCreationDto).getGoalId();

        // when
        // 생성한 목표를 삭제
        goalService.deleteGoalForMember(savedMemberId, savedMemberId, savedGoalId);

        // then
        // 삭제한 데이터가 데베에 존재하지 않는지 확인
        boolean isPresent = goalRepository.findById(savedGoalId).isPresent();
        assertThat(isPresent).isFalse();
    }

}