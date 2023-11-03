package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void createUser() {
        //given
        MemberDto memberDto = MemberDto.builder()
                .username("aaa")
                .nickname("gwan")
                .userStatusMessage("fighting!!!")
                .profileImgPath("None")
                .build();

        //when
        Member saveId = memberService.createUser(memberDto);

        //then
        Member findMember = memberRepository.findByUsername(memberDto.getUsername()).orElse(null);
        assertNotNull(findMember);
        assertEquals(memberDto.getUsername(), findMember.getUsername());
        assertEquals(memberDto.getNickname(), findMember.getNickname());
        assertEquals(memberDto.getUserStatusMessage(), findMember.getUserStatusMessage());
        assertEquals(memberDto.getProfileImgPath(), findMember.getProfileImgPath());
    }

    @Test
    void EditMember() {
        MemberDto memberDto = MemberDto.builder()
                .username("aaa")
                .nickname("gwan")
                .userStatusMessage("fighting!!!")
                .profileImgPath("None")
                .build();
        memberService.createUser(memberDto);
        memberService.editMember("aaa", "hwi", "good!", "Null");
        Member result = memberRepository.findByUsername("aaa").orElse(null);
        assertEquals(result.getUsername(), "aaa");
        assertEquals(result.getNickname(), "hwi");
        assertEquals(result.getUserStatusMessage(), "good!");
        assertEquals(result.getProfileImgPath(), "Null");

    }

}
