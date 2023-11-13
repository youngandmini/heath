package heavysnow.heath.controller;

import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.dto.GoalCreationDto;
import heavysnow.heath.dto.GoalIdResponseDto;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.exception.UnauthorizedException;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.service.GoalService;
import heavysnow.heath.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final GoalService goalService;

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    @PatchMapping("/members/{memberId}")
    public ResponseEntity<Void> updateMember(@PathVariable Long memberId, @RequestBody MemberDto memberDto,
                                             HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        memberService.editMember(loginMemberId, memberId, memberDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("members/{memberId}/goals")
    public ResponseEntity<GoalIdResponseDto> addGoal(@PathVariable Long memberId, @RequestBody GoalCreationDto goalCreationDto,
                                        HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        GoalIdResponseDto dto = goalService.createGoalForMember(loginMemberId, memberId, goalCreationDto);
        return ResponseEntity.ok(dto);
    }
}
