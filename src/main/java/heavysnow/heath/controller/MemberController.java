package heavysnow.heath.controller;

import heavysnow.heath.common.CookieManager;
import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.dto.goal.GoalCreateRequest;
import heavysnow.heath.dto.goal.GoalCreateResponse;
import heavysnow.heath.dto.goal.GoalUpdateRequest;
import heavysnow.heath.dto.member.MemberRequest;
import heavysnow.heath.dto.member.MemberResponse;
import heavysnow.heath.dto.post.PostDatesResponse;
import heavysnow.heath.dto.post.PostListResponse;
import heavysnow.heath.exception.UnauthorizedException;
import heavysnow.heath.service.GoalService;
import heavysnow.heath.service.MemberService;
import heavysnow.heath.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final GoalService goalService;
    private final MemberService memberService;
    private final PostService postService;

    /**
     * 마이페이지에서 회원 정보와 목표를 가져오기 위한 요청
     * @param memberId: 조회할 멤버 아이디
     * @return: 회원 정보와 목표들을 반환
     */
    @GetMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public MemberResponse getMember(@PathVariable("memberId") Long memberId) {
        log.info("회원id {}에 대한 정보 및 목표 가져오기 요청" , memberId);
        MemberResponse response = memberService.findMemberWithGoals(memberId);
        log.info("회원id {}에 대한 정보 및 목표 성공적으로 가져옴." , memberId);
        return response;
    }


    /**
     * 마이페이지에서 해당 년도 해당 월의 운동 정보 가져오기 위한 요청
     * @param memberId: 조회할 멤버 아이디
     * @param year: 조회할 년도
     * @param month: 조회할 월
     * @return: 해당 년도 해당 월의 운동 정보를 리스트 형태로 반환
     */
    @GetMapping("/{memberId}/dates")
    @ResponseStatus(HttpStatus.OK)
    public PostDatesResponse getExerciseDates(@PathVariable("memberId") Long memberId, @RequestParam("year") int year, @RequestParam("month") int month) {
        log.info("회원id {}, 년도 : {}, 월 : {}에 대한 운동 날짜 정보 요청" ,memberId, year, month);
        PostDatesResponse response = postService.getPostDates(memberId, year, month);
        log.info("회원id {}에 대한 운동 날짜 정보 성공적으로 가져옴." , memberId);
        return response;
    }

    /**
     * 마이페이지에서 회원 운동 인증글(포스트) 가져오기 위한 요청
     * @param memberId: 조회할 멤버 아이디
     * @param page: 페이지 번호
     * @return: 인증글을 9개씩 페이징하여 반환
     */
    @GetMapping("/{memberId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public PostListResponse getMemberPosts(@PathVariable("memberId") Long memberId, @RequestParam(value = "page", defaultValue = "1") int page) {
        log.info("회원id {} 에 대한 포스트 정보 가져오기 page : {} 요청" ,memberId, page);
        PostListResponse response = postService.getPostListByMember(memberId, page);
        log.info("회원id {} 에 대한 포스트 정보 가져오기 page : {} 성공" ,memberId, page);
        return response;
    }


    /**
     * 특정 회원을 탈퇴시키기 위한 요청
     * @param memberId: 삭제할 멤버 아이디
     * @param request: 로그인 정보
     */
    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMember(@PathVariable("memberId") Long memberId, HttpServletRequest request) {
        // 인증 토큰 확인
        log.info("회원id {}에 대한 삭제 시도" , memberId);
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        // 회원 탈퇴
        memberService.deleteMember(memberId, loginMemberId);
        log.info("회원id {}에 대한 삭제 성공" , memberId);
    }

    /**
     * 특정 회원의 정보를 수정하기 위한 요청
     * @param memberId: 수정할 회원 아이디
     * @param memberRequest: 수정할 회원 정보
     * @param request: 로그인 정보
     */
    @PatchMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMember(@PathVariable("memberId") Long memberId, @RequestBody @Valid MemberRequest memberRequest,
                                             HttpServletRequest request) {
        log.info("회원정보 수정 요청 발생");
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("수정되는 회원정보 {}, {}, {}", memberRequest.getUsername(), memberRequest.getNickname(), memberRequest.getUserStatusMessage());

        memberService.editMember(loginMemberId, memberId, memberRequest);
        log.info("수정 완료");
    }

    /**
     * 특정 회원의 새로운 목표를 생성하기 위한 요청
     * @param memberId: 멤버 아이디
     * @param goalCreateRequest: 목표 내용
     * @param request: 로그인 정보
     * @return: 생성된 목표의 아이디
     */
    @PostMapping("/{memberId}/goals")
    @ResponseStatus(HttpStatus.OK)
    public GoalCreateResponse addGoal(@PathVariable("memberId") Long memberId, @RequestBody GoalCreateRequest goalCreateRequest,
                                      HttpServletRequest request) {
        log.info("목표 추가요청 발생");
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("목표 생성 내용: {}", goalCreateRequest.toString());
        GoalCreateResponse response = goalService.createGoalForMember(loginMemberId, memberId, goalCreateRequest);
        log.info("생성 완료");
        return response;
    }

    /**
     * 회원 목표를 성공 / 미성공으로 변경하기 위한 요청
     * @param memberId: 수정할 목표가 속한 멤버 아이디
     * @param goalId: 수정할 목표 아이디
     * @param goalUpdateRequest: 달성 여부
     * @param request: 로그인 정보
     */
    @PatchMapping("/{memberId}/goals/{goalId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateGoal(@PathVariable("memberId") Long memberId, @PathVariable("goalId") Long goalId,
                           @RequestBody GoalUpdateRequest goalUpdateRequest, HttpServletRequest request) {
        log.info("목표 수정요청 발생");
        // 인증 토큰 확인
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("목표 수정 정보: {}", goalUpdateRequest.toString());
        goalService.updateGoalForMember(loginMemberId, memberId, goalId, goalUpdateRequest);
        log.info("수정 완료");
    }

    /**
     * 특정 회원의 특정 목표를 삭제하기 위한 요청
     * @param memberId: 삭제할 목표가 속한 멤버 아이디
     * @param goalId: 삭제할 목표 아이디
     * @param request: 로그인 정보
     */
    @DeleteMapping("/{memberId}/goals/{goalId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGoal(@PathVariable("memberId") Long memberId, @PathVariable("goalId") Long goalId,
                                           HttpServletRequest request) {
        log.info("목표 삭제 요청 발생");
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        goalService.deleteGoalForMember(loginMemberId, memberId, goalId);
        log.info("삭제 완료");
    }
}