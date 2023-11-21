package heavysnow.heath.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.login.LoginResponse;
import heavysnow.heath.dto.member.MemberRequest;
import heavysnow.heath.exception.BadRequestException;
import heavysnow.heath.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final LoginMemberHolder loginMemberHolder;

    /**
     * token을 받아서 로그아웃 수행
     * @param token: 해당 토큰으로 로그아웃 수행
     */
    public void logout(String token) {
        if (token == null) {
            return;
        }
        loginMemberHolder.logout(token);
    }

    /**
     * 로그인 요청이 들어왔을때, 토큰을 파싱하여 회원을 식별한다음,
     * 이미 존재한 회원이라면 로그인하고
     * 처음 방문한 회원이라면 로그인 정보를 저장한 다음 로그인을 수행
     * @param token: 해당 토큰으로 로그인을 수행
     */
    public LoginResponse login(String token) {
        if (token == null) {
            throw new BadRequestException();
        }
        Map<String, String> map = decodeToken(token);

        String email = map.get("email");
        String nickname = map.get("name");
        String profileImage = map.get("picture");

        Optional<Member> findMemberOptional = memberRepository.findByUsername(email);
        if (findMemberOptional.isPresent()) {
            Member findMember = findMemberOptional.get();
            doLogin(token, findMember.getId());

            return LoginResponse.of(findMember.getId());
        } else {
            //처음 방문한 회원이라면, 회원정보를 DB에 저장 후 로그인
            Long joinedMemberId = joinMember(email, nickname, profileImage);
            doLogin(token, joinedMemberId);

            return LoginResponse.of(joinedMemberId);
        }
    }

    //로그인 수행
    private void doLogin(String token, Long memberId) {
        loginMemberHolder.login(token, memberId);
    }

    //회원 가입
    private Long joinMember(String email, String nickname, String profileImage) {
        MemberRequest memberRequest = new MemberRequest(email, nickname, null, profileImage);
        return memberService.createUser(memberRequest);
    }

    // 토큰 파싱
    private Map<String, String> decodeToken(String token) {
        ObjectMapper mapper = new ObjectMapper();
        Decoder decoder = Base64.getDecoder();

        try {
            String[] chunks = token.split("\\.");
            String payload = new String(decoder.decode(chunks[1]));

            Map<String, String> map = mapper.readValue(payload, Map.class);
            return map;
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }
}
