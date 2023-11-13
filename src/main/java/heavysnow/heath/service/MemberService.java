package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.member.MemberRequest;
import heavysnow.heath.dto.member.MemberResponse;
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


    /**
     * 새로운 회원을 생성하는 메서드
     * @param memberDto: 이 정보를 이용해 회원가입
     * @return: 회원의 id를 반환
     */
    @Transactional
    public Long createUser(MemberRequest memberDto){
        Member savedMember = memberRepository.save(memberDto.toEntity());
        return savedMember.getId();
    }

    /**
     * 회원 정보를 수정하는 메서드
     * @param loginMemberId: 해당 멤버로 접속한 사용자가
     * @param memberId: 해당 멤버에
     * @param memberDto: 이 정보를 이용해 수정 요청
     */
    @Transactional
    public void editMember(Long loginMemberId, Long memberId, MemberRequest memberDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);

        if (!loginMemberId.equals(memberId)) {
            throw new ForbiddenException();
        }

        member.update(memberDto.getNickname(), memberDto.getUserStatusMessage(), memberDto.getProfileImgUrl());
    }

    /**
     * 회원을 탈퇴하는 메서드
     * @param memberId: 해당 멤버에 대해
     * @param LoginMemberId: 해당 접속한 멤버가 탍퇴 요청
     */
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

    /**
     * 해당 회원의 정보를 목표와 함께 조회하는 메서드
     * @param memberId: 해당 멤버의 정보를 요청
     * @return: 멤버 + 목표 정보
     */
    public MemberResponse findMemberWithGoals(Long memberId){
        Member member = memberRepository.findByIdWithGoals(memberId).orElseThrow(NotFoundException::new);

        return MemberResponse.of(member);
    }
}
