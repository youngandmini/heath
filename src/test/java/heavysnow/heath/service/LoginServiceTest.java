package heavysnow.heath.service;

import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.domain.Member;
import heavysnow.heath.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    private LoginService loginService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void login() {
        //given

        // "email": "example@gmail.com",
        // "name": "exampleNickname",
        // "picture": "picture.example.com",
        // "iat": 1516239022
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImV4YW1wbGVAZ21haWwuY29tIiwibmFtZSI6ImV4YW1wbGVOaWNrbmFtZSIsInBpY3R1cmUiOiJwaWN0dXJlLmV4YW1wbGUuY29tIiwiaWF0IjoxNTE2MjM5MDIyfQ.2VikaS9b1rwScZf9YPB68AZNDL_GBM_hZ4Hlgh-qaLc";

        //when
        loginService.login(token);
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(token);
        Long loginMemberId = loginMemberIdOptional.get();
        Optional<Member> findMemberOptional = memberRepository.findById(loginMemberId);
        Member findMember = findMemberOptional.get();

        System.out.println("findMember = " + findMember.getId());
        System.out.println("findMember = " + findMember.getNickname());
        System.out.println("findMember = " + findMember.getUsername());
        System.out.println("findMember.getProfileImgUrl() = " + findMember.getProfileImgUrl());
        
        //then
        assertThat(findMember.getNickname()).isEqualTo("exampleNickname");
        assertThat(findMember.getProfileImgUrl()).isEqualTo("picture.example.com");
        assertThat(findMember.getUsername()).isEqualTo("example@gmail.com");

    }

    @Test
    void logout() {
        //given

        // "email": "example@example.com",
        // "name": "exampleNickname",
        // "image": "example.google.com",
        // "iat": 1516239022
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImV4YW1wbGVAZXhhbXBsZS5jb20iLCJuYW1lIjoiZXhhbXBsZU5pY2tuYW1lIiwiaW1hZ2UiOiJleGFtcGxlLmdvb2dsZS5jb20iLCJpYXQiOjE1MTYyMzkwMjJ9.M_6l8ZC7rOssiZqRcW_gEXbK8zCCskUm9rtzAOe9VsI";

        //when
        loginService.login(token);
        loginService.logout(token);

        //then
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(token);
        assertThat(loginMemberIdOptional.isEmpty()).isTrue();

    }
}