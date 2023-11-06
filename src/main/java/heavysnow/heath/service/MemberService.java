package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member createUser(MemberDto dto){
        return memberRepository.save(dto.toEntity());
    }

    @Transactional
    public void editMember(String userName, String nickName, String userStatusMessage, String profileImgUrl) {
        Member entity = memberRepository.findByUsername(userName).orElseThrow();
        entity.update(nickName, userStatusMessage, profileImgUrl);
    }
}
