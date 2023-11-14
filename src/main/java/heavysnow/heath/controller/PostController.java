package heavysnow.heath.controller;

import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.dto.comment.CommentCreateRequest;
import heavysnow.heath.dto.comment.CommentCreateResponse;
import heavysnow.heath.dto.comment.CommentUpdateRequest;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.dto.post.PostAddResponse;
import heavysnow.heath.dto.post.PostEditRequest;
import heavysnow.heath.dto.post.PostDetailResponse;
import heavysnow.heath.dto.post.PostListResponse;
import heavysnow.heath.exception.UnauthorizedException;
import heavysnow.heath.service.CommentService;
import heavysnow.heath.service.LikedService;
import heavysnow.heath.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
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
    public PostListResponse getPostList(@RequestParam("page") int page, @RequestParam("sort") String sort) {

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
    public PostAddResponse writePost(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        return postService.writePost(loginMemberId, postAddRequest);
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
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        postService.editPost(postId, postEditRequest, loginMemberId);
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
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElse(null);

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
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        postService.deletePost(postId, loginMemberId);
    }

    /**
     * 좋아요 상태를 수정하기 위한 요청
     * @param postId: 좋아요를 달 게시글 아이디
     * @param request: 로그인 정보
     */
    @PostMapping("/{postId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public void likesPost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        likedService.changeMemberPostLiked(postId, loginMemberId);
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
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        return commentService.createComment(postId, commentDto, null, loginMemberId);
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
    public CommentCreateResponse addReply(@RequestBody CommentCreateRequest commentDto, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        return commentService.createComment(postId, commentDto, commentId, loginMemberId);
    }

    /**
     * 특정 댓글을 수정하기 위한 요청
     * @param commentDto: 수정할 댓글 내용
     * @param postId: 수정할 댓글이 속한 게시글 아이디
     * @param commentId: 수정할 댓글 아이디
     * @param request: 로그인 정보
     */
    @PatchMapping("/{postId}/comments/{commentId}")
    public void updateComment(@RequestBody CommentUpdateRequest commentDto, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        commentService.updateComment(postId, commentId, commentDto, loginMemberId);
    }

    /**
     * 특정 댓글을 삭제하기 위한 요청
     * @param commentId: 삭제할 댓글 아이디
     * @param request: 로그인 정보
     */
    @DeleteMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable("commentId") Long commentId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        commentService.deleteComment(commentId, loginMemberId);
    }

}
