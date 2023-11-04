package heavysnow.heath.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import heavysnow.heath.domain.Goal;
import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.GoalCreationDto;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.repository.GoalRepository;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.service.GoalService;
import heavysnow.heath.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트
class GoalServiceTest {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private GoalService goalService;

    @Autowired
    MemberService memberService;

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
}