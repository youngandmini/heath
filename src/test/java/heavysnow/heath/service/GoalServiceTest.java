package heavysnow.heath.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import heavysnow.heath.domain.Goal;
import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.GoalCreationDto;
import heavysnow.heath.dto.GoalUpdateDto;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.repository.GoalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Member member = memberService.createUser(memberDto);

        // 멤버 id
        Long userId = member.getId();


        final String content = "content";
        final boolean isAchieved = false;

        // Goal DTO
        GoalCreationDto goalCreationDto = new GoalCreationDto(content, isAchieved);


        // when
        // 해당 멤버의 목표 데베 저장
        Goal createdGoal = goalService.createGoalForMember(userId, goalCreationDto);


        // then
        assertThat(createdGoal.getMember().getId()).isEqualTo(member.getId());  // 같은 멤버인지 검증
        assertThat(createdGoal.getContent()).isEqualTo(goalCreationDto.getContent());
        assertThat(createdGoal.isAchieved()).isEqualTo(goalCreationDto.isAchieved());
    }

    @DisplayName("updateGoalForMember : 특정 멤버에 대한 목표를 수정한다.")
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
        Member member = memberService.createUser(memberDto);

        // 멤버 id
        Long userId = member.getId();


        // Goal DTO
        GoalCreationDto goalCreationDto1 = new GoalCreationDto("content1", false);
        GoalCreationDto goalCreationDto2 = new GoalCreationDto("content2", true);


        // 해당 멤버의 목표 데베 저장
        Goal createdGoal1 = goalService.createGoalForMember(userId, goalCreationDto1);
        Goal createdGoal2 = goalService.createGoalForMember(userId, goalCreationDto2);

        String updatedContent = "update content 1";
        Boolean isAchieved = true;

        GoalUpdateDto updateDto = new GoalUpdateDto(updatedContent, isAchieved);


        // when
        Goal updateGoal = goalService.updateGoalForMember(member.getId(), createdGoal1.getId(), updateDto);

        // then
        assertThat(updateGoal.getContent()).isEqualTo(updatedContent);
        assertThat(updateGoal.isAchieved()).isEqualTo(isAchieved);

        Goal persistentGoal = goalRepository.findById(createdGoal1.getId()).orElseThrow();
        assertThat(persistentGoal.getContent()).isEqualTo(updatedContent);
        assertThat(persistentGoal.isAchieved()).isEqualTo(isAchieved);
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
        Member member = memberService.createUser(memberDto);

        // 멤버 id
        Long userId = member.getId();

        // Goal DTO
        GoalCreationDto goalCreationDto = new GoalCreationDto("1일 1커밋 하기", false);

        // 멤버의 목표 데베에 저장
        Goal createdGoal = goalService.createGoalForMember(userId, goalCreationDto);

        // when
        // 생성한 목표를 삭제
        goalService.deleteGoalForMember(userId, createdGoal.getId());

        // then
        // 삭제한 데이터가 데베에 존재하지 않는지 확인
        boolean isPresent = goalRepository.findById(createdGoal.getId()).isPresent();
        assertThat(isPresent).isFalse();
    }

}