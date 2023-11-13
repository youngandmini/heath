package heavysnow.heath.controller;

import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.dto.*;
import heavysnow.heath.dto.postdto.PostListResponseDto;
import heavysnow.heath.exception.ForbiddenException;
import heavysnow.heath.exception.UnauthorizedException;
import heavysnow.heath.service.GoalService;
import heavysnow.heath.service.MemberService;
import heavysnow.heath.service.PostService;
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

    private final PostService postService;

    /**
     * 마이페이지 회원 정보 + 목표가져오는 메서드
     *
     * @param memberId 조회할 멤버 아이디
     * @return ResponseEntity
     */
    @GetMapping("members/{memberId}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long memberId) {
        MemberResponseDto response = memberService.findMemberWithGoals(memberId);
        return ResponseEntity.ok(response);
    }


    /**
     * 마이페이지 해당 년도 해당 월의 운동 정보 가져오는 메서드
     *
     * @param memberId 조회할 멤버 아이디
     * @param year     조회할 년도
     * @param month    조회할 월
     * @return ResponseEntity<PostDatesResponseDto>
     */
    @GetMapping("members/{memberId}/dates")
    public ResponseEntity<PostDatesResponseDto> getExerciseDates(@PathVariable Long memberId, @RequestParam int year, @RequestParam int month) {
        PostDatesResponseDto response = postService.getPostDates(memberId, year, month);
        return ResponseEntity.ok(response); // 200 OK
    }

    /**
     * 회원 운도인증글(포스트) 가져오기(최대 9개로 제한한다.)
     *
     * @param memberId 조회할 멤버 아이디
     * @param page     게시글 수
     * @return ResponseEntity<PostListResponseDto>
     */
    @GetMapping("members/{memberId}/posts")
    public ResponseEntity<PostListResponseDto> getMemberPosts(@PathVariable Long memberId, @RequestParam(defaultValue = "0") int page) {
        PostListResponseDto response = postService.getPostListByMember(memberId, page);
        return ResponseEntity.ok(response);
    }


    /**
     * 회원 목표를 성공 / 미성공 체크
     * @param memberId 수정할 멤버 아이디
     * @param goalId 수정할 목표 아이디
     * @param goalUpdateDto content, isAchieved
     * @param request 로그인 정보
     * @return ResponseEntity<Void>
     */
    @PatchMapping("members/{memberId}/goals/{goalId}")
    public ResponseEntity<Void> updateGoal(@PathVariable Long memberId, @PathVariable Long goalId, @RequestBody GoalUpdateDto goalUpdateDto, HttpServletRequest request) {
        // 인증 토큰 확인
        Optional<Long> loginMemberOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberOptional.orElseThrow(UnauthorizedException::new);

        // 권한 검증 : 로그인한 사용자가 자신의 목표를 등록하는지 확인
        if (!loginMemberId.equals(memberId)) {
            throw new ForbiddenException();
        }

        goalService.updateGoalForMember(memberId, goalId, goalUpdateDto);

        return ResponseEntity.ok().build();
    }

    /**
     * 회원 탈퇴
     * @param memberId 삭제할 멤버 아이디
     * @param request 로그인 정보
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId, HttpServletRequest request) {
        // 인증 토큰 확인
        Optional<Long> loginMemberOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberOptional.orElseThrow(UnauthorizedException::new);

        // 회원 탈퇴
        memberService.deleteMember(memberId, loginMemberId);

        return ResponseEntity.ok().build();
    }


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

    @DeleteMapping("members/{memberId}/goals/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long memberId, @PathVariable Long goalId,
                                           HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        goalService.deleteGoalForMember(loginMemberId, memberId, goalId);
        return ResponseEntity.ok().build();
    }
}
