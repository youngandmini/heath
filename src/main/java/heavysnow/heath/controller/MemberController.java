package heavysnow.heath.controller;

import heavysnow.heath.common.LoginMemberHolder;
<<<<<<< HEAD
import heavysnow.heath.dto.GoalUpdateDto;
import heavysnow.heath.dto.MemberResponseDto;
import heavysnow.heath.dto.PostDatesResponseDto;
import heavysnow.heath.dto.postdto.PostListResponseDto;
import heavysnow.heath.exception.ForbiddenException;
import heavysnow.heath.exception.UnauthorizedException;
import heavysnow.heath.service.GoalService;
import heavysnow.heath.service.MemberService;
import heavysnow.heath.service.PostService;
=======
import heavysnow.heath.dto.GoalCreationDto;
import heavysnow.heath.dto.GoalIdResponseDto;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.exception.UnauthorizedException;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.service.GoalService;
import heavysnow.heath.service.MemberService;
>>>>>>> origin/Gwanhwi
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

<<<<<<< HEAD
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final PostService postService;
    private final GoalService goalService;

    /**
     * 마이페이지 회원 정보 + 목표가져오는 메서드
     *
     * @param memberId 조회할 멤버 아이디
     * @return ResponseEntity
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long memberId) {
//        Optional<Long> loginMemberOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Long loginMemberId = loginMemberOptional.orElse(null);
//
//        // 추가적인 로직 생길 수도 있으니 분리
//        // 자기 자신의 페이지인 경우
//        if (loginMemberId != null && loginMemberId.equals(memberId)) {
//            MemberResponseDto response = memberService.findMemberWithGoals(loginMemberId);
//            return ResponseEntity.ok(response);
//        }
//        // 타인 페이지인 경우
//        else {
//            // 정보 제한 필요(목표는 수정할수 있어야함.)
//            MemberResponseDto response = memberService.findMemberWithGoals(memberId);
//            return ResponseEntity.ok(response);
//        }

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
    @GetMapping("/{memberId}/posts")
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
     * @return ResponseEntity<Void></>
     */
    @PatchMapping("/{memberId}/goals/{goalId}")
    public ResponseEntity<Void> updateGoal(@PathVariable Long memberId, @PathVariable Long goalId, @RequestBody GoalUpdateDto goalUpdateDto, HttpServletRequest request) {
        // 인증 토큰 확인
        Optional<Long> loginMemberOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberOptional.orElseThrow(() -> new UnauthorizedException("로그인이 필요합니다."));

        // 권한 검증 : 로그인한 사용자가 자신의 목표를 등록하는지 확인
        if (!loginMemberId.equals(memberId)) {
            throw new ForbiddenException("다른 회원의 목표를 수정할 수 없습니다.");
        }

        goalService.updateGoalForMember(memberId, goalId, goalUpdateDto);

        return ResponseEntity.ok().build();
    }



=======
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
>>>>>>> origin/Gwanhwi
}
