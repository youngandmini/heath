package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.goal.GoalCreationDto;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.exception.NotFoundException;
import heavysnow.heath.repository.GoalRepository;
import heavysnow.heath.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GoalService goalService;
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private EntityManager em;

    @Test
    void createUser() {
        //given
        MemberDto memberDto = MemberDto.builder()
                .username("aaa")
                .nickname("gwan")
                .userStatusMessage("fighting!!!")
                .profileImgUrl("None")
                .build();

        //when
        Long savedMemberId = memberService.createUser(memberDto);

        //then
        Member findMember = memberRepository.findByUsername(memberDto.getUsername()).orElse(null);
        assertNotNull(findMember);
        assertEquals(memberDto.getUsername(), findMember.getUsername());
        assertEquals(memberDto.getNickname(), findMember.getNickname());
        assertEquals(memberDto.getUserStatusMessage(), findMember.getUserStatusMessage());
        assertEquals(memberDto.getProfileImgUrl(), findMember.getProfileImgUrl());
    }

    @Test
    void EditMember() {
        MemberDto memberDto = MemberDto.builder()
                .username("aaa")
                .nickname("gwan")
                .userStatusMessage("fighting!!!")
                .profileImgUrl("None")
                .build();
        Long savedId = memberService.createUser(memberDto);
        MemberDto dto = MemberDto.builder()
                .username("aaa")
                .nickname("hwi")
                .userStatusMessage("good!")
                .profileImgUrl("Null")
                .build();
        memberService.editMember(savedId, savedId, dto);
        Member result = memberRepository.findById(savedId).orElse(null);
        assertEquals(result.getUsername(), dto.getUsername());
        assertEquals(result.getNickname(), dto.getNickname());
        assertEquals(result.getUserStatusMessage(), dto.getUserStatusMessage());
        assertEquals(result.getProfileImgUrl(), dto.getProfileImgUrl());

    }

    @Test
    void deleteMember() {
        MemberDto memberDto = MemberDto.builder()
                .username("aaaa@example.com")
                .nickname("gwan")
                .userStatusMessage("")
                .profileImgUrl("imgUrl")
                .build();
        Long savedId = memberService.createUser(memberDto);
        memberService.deleteMember(savedId, savedId);


        assertThatThrownBy(() -> memberService.findMemberWithGoals(savedId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("회원을 삭제하면 goal도 함께 문제 없이 삭제되어야한다.")
    void deleteMemberWithGoals() {
        MemberDto memberDto = MemberDto.builder()
                .username("aaaa@example.com")
                .nickname("gwan")
                .userStatusMessage("")
                .profileImgUrl("imgUrl")
                .build();
        Long savedId = memberService.createUser(memberDto);

        Long goalId1 = goalService.createGoalForMember(savedId, savedId, new GoalCreationDto("목표1", false)).getGoalId();
        Long goalId2 = goalService.createGoalForMember(savedId, savedId, new GoalCreationDto("목표2", false)).getGoalId();

        em.flush();
        em.clear();

        memberService.deleteMember(savedId, savedId);

        assertThatThrownBy(() -> memberService.findMemberWithGoals(savedId))
                .isInstanceOf(NotFoundException.class);

        assertThat(goalRepository.findById(goalId1).isEmpty()).isTrue();
        assertThat(goalRepository.findById(goalId2).isEmpty()).isTrue();

    }

}
