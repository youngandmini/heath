package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member createUser(MemberDto dto){
        Member encoding = Member.builder()
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .userStatusMessage(dto.getUserStatusMessage())
                .profileImgPath(dto.getProfileImgPath())
                .build();
        return memberRepository.save(encoding);
    }

    @Transactional
    public Member editMember(String userName, String nickName, String userStatusMessage, String profileImgPath) {
        Member entity = memberRepository.findByUsername(userName).orElse(null);
        if (entity == null){
            System.out.println("회원정보가 없습니다.");
        }
        entity.update(nickName, userStatusMessage, profileImgPath);
        return memberRepository.findByUsername("userName").orElse(null);
    }
}
