package heavysnow.heath.service;

import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.domain.Member;
import heavysnow.heath.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void login() {
        //given

        // "email": "example@example.com",
        // "name": "exampleNickname",
        // "image": "example.google.com",
        // "iat": 1516239022
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImV4YW1wbGVAZXhhbXBsZS5jb20iLCJuYW1lIjoiZXhhbXBsZU5pY2tuYW1lIiwiaW1hZ2UiOiJleGFtcGxlLmdvb2dsZS5jb20iLCJpYXQiOjE1MTYyMzkwMjJ9.M_6l8ZC7rOssiZqRcW_gEXbK8zCCskUm9rtzAOe9VsI";

        //when
        loginService.login(token);
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(token);
        Long loginMemberId = loginMemberIdOptional.get();
        Optional<Member> findMemberOptional = memberRepository.findById(loginMemberId);
        Member findMember = findMemberOptional.get();

        //then
        assertThat(findMember.getNickname()).isEqualTo("exampleNickname");
        assertThat(findMember.getProfileImgUrl()).isEqualTo("example.google.com");
        assertThat(findMember.getUsername()).isEqualTo("example@example.com");

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