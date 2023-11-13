package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
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
        Long tokenId = memberRepository.findById(savedId).orElseThrow().getId();
        memberService.editMember(tokenId, savedId, dto);
        Member result = memberRepository.findById(savedId).orElse(null);
        assertEquals(result.getUsername(), dto.getUsername());
        assertEquals(result.getNickname(), dto.getNickname());
        assertEquals(result.getUserStatusMessage(), dto.getUserStatusMessage());
        assertEquals(result.getProfileImgUrl(), dto.getProfileImgUrl());

    }

}
