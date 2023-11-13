package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.dto.MemberResponseDto;
import heavysnow.heath.exception.ForbiddenException;
import heavysnow.heath.exception.NotFoundException;
import heavysnow.heath.exception.UnauthorizedException;
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
    public Long createUser(MemberDto dto){
        Member savedMember = memberRepository.save(dto.toEntity());
        return savedMember.getId();
    }

    @Transactional
    public void editMember(Long tokenId, Long memberId, MemberDto dto) {
        if (!tokenId.equals(memberId)) {
            throw new ForbiddenException();
        }
        Member entity = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        entity.update(dto.getNickname(), dto.getUserStatusMessage(), dto.getProfileImgUrl());
    }

    //fetch, join
    //Member를 반환하는 것에서 MemberResponseDto를 반환하도록 변경
    public MemberResponseDto findMemberWithGoals(Long memberId){
        Member member = memberRepository.findByIdWithGoals(memberId).orElseThrow();

        return MemberResponseDto.of(member);
    }
}
