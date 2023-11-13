package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.member.MemberRequest;
import heavysnow.heath.dto.member.MemberResponseDto;
import heavysnow.heath.exception.ForbiddenException;
import heavysnow.heath.exception.NotFoundException;
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
    public Long createUser(MemberRequest dto){
        Member savedMember = memberRepository.save(dto.toEntity());
        return savedMember.getId();
    }

    @Transactional
    public void editMember(Long loginMemberId, Long memberId, MemberRequest dto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);

        if (!loginMemberId.equals(memberId)) {
            throw new ForbiddenException();
        }

        member.update(dto.getNickname(), dto.getUserStatusMessage(), dto.getProfileImgUrl());
    }

    @Transactional
    public void deleteMember(Long memberId, Long LoginMemberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        if (!findMember.getId().equals(LoginMemberId)) {
            throw new ForbiddenException();
        }

        memberRepository.delete(findMember);
    }

    //fetch, join
    //Member를 반환하는 것에서 MemberResponseDto를 반환하도록 변경
    public MemberResponseDto findMemberWithGoals(Long memberId){
        Member member = memberRepository.findByIdWithGoals(memberId).orElseThrow(NotFoundException::new);

        return MemberResponseDto.of(member);
    }
}
