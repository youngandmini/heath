package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.dto.MemberResponseDto;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Member createUser(MemberDto dto){
        return memberRepository.save(dto.toEntity());
    }

    @Transactional
    public void editMember(Long memberId, MemberDto dto) {
        Member entity = memberRepository.findById(memberId).orElseThrow();
        entity.update(dto.getNickname(), dto.getUserStatusMessage(), dto.getProfileImgUrl());
    }

    //fetch, join
    //Member를 반환하는 것에서 MemberResponseDto를 반환하도록 변경
    public MemberResponseDto findMemberWithGoals(Long memberId){
        Member member = memberRepository.findByIdWithGoals(memberId).orElseThrow();

        return MemberResponseDto.of(member);
    }
}
