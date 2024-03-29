package heavysnow.heath.controller;

import heavysnow.heath.common.CookieManager;
import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.dto.comment.CommentCreateRequest;
import heavysnow.heath.dto.comment.CommentCreateResponse;
import heavysnow.heath.dto.comment.CommentUpdateRequest;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.dto.post.PostAddResponse;
import heavysnow.heath.dto.post.PostEditRequest;
import heavysnow.heath.dto.post.PostDetailResponse;
import heavysnow.heath.dto.post.PostListResponse;
import heavysnow.heath.exception.BadRequestException;
import heavysnow.heath.exception.UnauthorizedException;
import heavysnow.heath.service.CommentService;
import heavysnow.heath.service.LikedService;
import heavysnow.heath.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final LikedService likedService;

    /**
     * 메인페이지에서 게시글 리스트를 요청
     * @param page: 페이지 번호
     * @param sort: 정렬 조건
     * @return: 게시글 리스트를 3개씩 반환
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PostListResponse getPostList(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "sort", defaultValue = "createdDate") String sort) {

        log.info("게시글 리스트 조회 요청 발생. 요청한 페이지: {}, 정렬 기준: {}", page, sort);
        if (!(sort.equals("createdDate") || sort.equals("liked"))) {
            throw new BadRequestException();
        }

        return postService.getPostList(page, sort);
    }

    /**
     * 새로운 게시글을 등록하기 위한 요청
     * @param postAddRequest: 등록할 게시글 정보
     * @param request: 로그인 정보
     * @return: 등록한 게시글의 아이디
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PostAddResponse writePost(@RequestBody @Valid PostAddRequest postAddRequest, HttpServletRequest request) {
        log.info("새로운 게시글 등록 요청 발생");
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("새로운 게시글 등록 요청 정보: {}, {}, {}", postAddRequest.getTitle(), postAddRequest.getContent(), postAddRequest.getPostImgUrls());
        log.info("새로운 게시글 등록 요청자: {}", loginMemberId);

        PostAddResponse postAddResponse = postService.writePost(loginMemberId, postAddRequest);
        log.info("새로운 게시글 등록 완료: {}", postAddResponse.getPostId());
        return postAddResponse;
    }

    /**
     * 게시글을 수정하기 위한 요청
     * @param postId: 수정할 게시글 아이디
     * @param postEditRequest: 수정할 게시글 정보
     * @param request: 로그인 정보
     */
    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void editPost(@PathVariable("postId") Long postId,
                         @RequestBody PostEditRequest postEditRequest,
                         HttpServletRequest request) {
        log.info("게시글 수정 요청 발생. 요청한 게시글: {}", postId);
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("게시글 수정 요청 정보: {}, {}, {}", postEditRequest.getTitle(), postEditRequest.getContent(), postEditRequest.getPostImgUrls());
        log.info("게시글 수정 요청자: {}", loginMemberId);

        postService.editPost(postId, postEditRequest, loginMemberId);
        log.info("게시글 수정 요청 수락");
    }

    /**
     * 게시글 상세를 조회하기 위한 요청
     * @param postId: 조회할 게시글 아이디
     * @param request: 로그인 정보
     * @return: 게시글 상세 정보
     */
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDetailResponse detailedPost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        log.info("게시글 상세 조회 요청 발생. 요청한 게시글: {}", postId);
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElse(null);
        log.info("게시글 상세 조회 요청자: {}", loginMemberId);

        return postService.getPostWithDetail(postId, loginMemberId);
    }


    /**
     * 게시글을 삭제하기 위한 요청
     * @param postId: 삭제할 게시글 아이디
     * @param request: 로그인 정보
     */
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        log.info("게시글 삭제 요청 발생. 요청한 게시글: {}", postId);
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("게시글 삭제 요청자: {}", loginMemberId);

        postService.deletePost(postId, loginMemberId);
        log.info("게시글 삭제 요청 수락");
    }

    /**
     * 좋아요 상태를 수정하기 위한 요청
     * @param postId: 좋아요를 달 게시글 아이디
     * @param request: 로그인 정보
     */
    @PostMapping("/{postId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public void likesPost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        log.info("게시글 좋아요 요청 발생");
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("게시글 정보: {}", postId);
        log.info("게시글 좋아요 요청자: {}", loginMemberId);

        likedService.changeMemberPostLiked(postId, loginMemberId);
        log.info("게시글 좋아요 완료");
    }

    /**
     * 새로운 댓글을 달기 위한 요청
     * @param commentDto: 댓글 내용
     * @param postId: 댓글을 달 게시글 아이디
     * @param request: 로그인 정보
     * @return: 댓글 아이디
     */
    @PostMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentCreateResponse addComment(@RequestBody CommentCreateRequest commentDto, @PathVariable("postId") Long postId, HttpServletRequest request) {
        log.info("새로운 댓글 등록 요청 발생");
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("새로운 댓글 등록 요청 정보: {}, {}", commentDto.getContent(), postId);
        log.info("새로운 댓글 등록 요청자: {}", loginMemberId);

        CommentCreateResponse commentCreateResponse = commentService.createComment(postId, commentDto, null, loginMemberId);
        log.info("새로운 댓글 등록 완료: {}", commentCreateResponse.getCommentId());
        return commentCreateResponse;
    }

    /**
     * 특정 댓글에 답글을 달기 위한 요청
     * @param commentDto: 답글 내용
     * @param postId: 답글을 달 댓글이 존재하는 게시글 아이디
     * @param commentId: 답글을 달 댓글 아이디
     * @param request: 로그인 정보
     * @return: 답글 아이디
     */
    @PostMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentCreateResponse addReply(@RequestBody CommentCreateRequest commentDto,
                                          @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId,
                                          HttpServletRequest request) {
        log.info("새로운 답글 등록 요청 발생");
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("새로운 답글 등록 요청 정보: {}, {}, {}", commentDto.getContent(), postId, commentId);
        log.info("새로운 답글 등록 요청자: {}", loginMemberId);


        CommentCreateResponse commentCreateResponse = commentService.createComment(postId, commentDto, commentId, loginMemberId);
        log.info("새로운 답글 등록 완료: {}", commentCreateResponse.getCommentId());
        return commentCreateResponse;
    }

    /**
     * 특정 댓글을 수정하기 위한 요청
     * @param commentDto: 수정할 댓글 내용
     * @param postId: 수정할 댓글이 속한 게시글 아이디
     * @param commentId: 수정할 댓글 아이디
     * @param request: 로그인 정보
     */
    @PatchMapping("/{postId}/comments/{commentId}")
    public void updateComment(@RequestBody CommentUpdateRequest commentDto,
                              @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId,
                              HttpServletRequest request) {
        log.info("댓글 수정 요청 발생");
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("댓글 수정 요청 정보: {}, {}, {}", commentDto.getContent(), postId, commentId);
        log.info("댓글 수정 요청자: {}", loginMemberId);

        commentService.updateComment(postId, commentId, commentDto, loginMemberId);
        log.info("댓글 수정 완료");
    }

    /**
     * 특정 댓글을 삭제하기 위한 요청
     * @param commentId: 삭제할 댓글 아이디
     * @param request: 로그인 정보
     */
    @DeleteMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId,
                              HttpServletRequest request) {
        log.info("댓글 삭제 요청 발생");
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
//        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(CookieManager.findLoginSessionCookie(request));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);
        log.info("댓글 삭제 요청 정보: {}, {}", postId, commentId);
        log.info("댓글 삭제 요청자: {}", loginMemberId);

        commentService.deleteComment(commentId, loginMemberId);
        log.info("댓글 삭제 완료");
    }
}
