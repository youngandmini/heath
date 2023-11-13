package heavysnow.heath.controller;

import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.dto.*;
import heavysnow.heath.dto.postdto.PostListResponseDto;
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
@RequestMapping("/members")
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
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable("memberId") Long memberId) {
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
    @GetMapping("/{memberId}/dates")
    public ResponseEntity<PostDatesResponseDto> getExerciseDates(@PathVariable("memberId") Long memberId, @RequestParam("year") int year, @RequestParam("month") int month) {
        PostDatesResponseDto response = postService.getPostDates(memberId, year, month);
        return ResponseEntity.ok(response); // 200 OK
    }

    /**
     * 회원 운동 인증글(포스트) 가져오기(9개씩 불러온다.)
     *
     * @param memberId 조회할 멤버 아이디
     * @param page     게시글 수
     * @return ResponseEntity<PostListResponseDto>
     */
    @GetMapping("/{memberId}/posts")
    public ResponseEntity<PostListResponseDto> getMemberPosts(@PathVariable("memberId") Long memberId, @RequestParam(value = "page", defaultValue = "0") int page) {
        PostListResponseDto response = postService.getPostListByMember(memberId, page);
        return ResponseEntity.ok(response);
    }


    /**
     * 회원 탈퇴
     * @param memberId 삭제할 멤버 아이디
     * @param request 로그인 정보
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable("memberId") Long memberId, HttpServletRequest request) {
        // 인증 토큰 확인
        Optional<Long> loginMemberOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberOptional.orElseThrow(UnauthorizedException::new);

        // 회원 탈퇴
        memberService.deleteMember(memberId, loginMemberId);

        return ResponseEntity.ok().build();
    }


    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> updateMember(@PathVariable("memberId") Long memberId, @RequestBody MemberDto memberDto,
                                             HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        memberService.editMember(loginMemberId, memberId, memberDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{memberId}/goals")
    public ResponseEntity<GoalIdResponseDto> addGoal(@PathVariable("memberId") Long memberId, @RequestBody GoalCreationDto goalCreationDto,
                                                     HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        GoalIdResponseDto response = goalService.createGoalForMember(loginMemberId, memberId, goalCreationDto);
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
    @PatchMapping("/{memberId}/goals/{goalId}")
    public ResponseEntity<Void> updateGoal(@PathVariable("memberId") Long memberId, @PathVariable("goalId") Long goalId, @RequestBody GoalUpdateDto goalUpdateDto, HttpServletRequest request) {
        // 인증 토큰 확인
        Optional<Long> loginMemberOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberOptional.orElseThrow(UnauthorizedException::new);

        goalService.updateGoalForMember(loginMemberId, memberId, goalId, goalUpdateDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberId}/goals/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable("memberId") Long memberId, @PathVariable("goalId") Long goalId,
                                           HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        goalService.deleteGoalForMember(loginMemberId, memberId, goalId);
        return ResponseEntity.ok().build();
    }
}