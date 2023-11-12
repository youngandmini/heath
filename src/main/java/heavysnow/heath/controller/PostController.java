package heavysnow.heath.controller;

import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.dto.CommentCreateDto;
import heavysnow.heath.dto.CommentCreateResponseDto;
import heavysnow.heath.dto.CommentUpdateDto;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.dto.post.PostAddResponse;
import heavysnow.heath.dto.post.PostEditRequest;
import heavysnow.heath.dto.postdto.PostDetailResponseDto;
import heavysnow.heath.dto.postdto.PostListResponseDto;
import heavysnow.heath.exception.UnauthorizedException;
import heavysnow.heath.service.CommentService;
import heavysnow.heath.service.LikedService;
import heavysnow.heath.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
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
     * 메인페이지
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PostListResponseDto getPostList(
            @RequestParam("page") int page,
            @RequestParam("sort") String sort
    ) {
        return postService.getPostList(page, sort);
    }

    /**
     * 게시글 등록 요청
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PostAddResponse writePost(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        postAddRequest.setMemberId(loginMemberId);
        return postService.writePost(postAddRequest);
    }


    /**
     * 게시글 상세 조회 요청
     */
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDetailResponseDto detailedPost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElse(null);

        return postService.getPostWithDetail(postId, loginMemberId);
    }

    /**
     * 게시글 수정 요청
     */
    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void editPost(@PathVariable("postId") Long postId,
                         @RequestBody PostEditRequest postEditRequest,
                         HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        postService.editPost(postEditRequest, loginMemberId);
    }


    /**
     * 게시글 삭제 요청
     */
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        postService.deletePost(postId, loginMemberId);
    }

    /**
     * 좋아요 요청
     */
    @PostMapping("/{postId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public void likesPost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        likedService.changeMemberPostLiked(postId, loginMemberId);
    }

    /**
     * 새로운 댓글 요청
     */
    @PostMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentCreateResponseDto addComment(@RequestBody CommentCreateDto commentDto, @PathVariable("postId") Long postId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        commentDto.setIds(postId, null, loginMemberId);

        return commentService.createComment(commentDto);
    }

    /**
     * 새로운 답글 요청
     */
    @PostMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentCreateResponseDto addReply(@RequestBody CommentCreateDto commentDto, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        commentDto.setIds(postId, commentId, loginMemberId);

        return commentService.createComment(commentDto);
    }

    /**
     * 댓글(답글) 수정
     */
    @PatchMapping("/{postId}/comments/{commentId}")
    public void updateComment(@RequestBody CommentUpdateDto commentDto, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        commentDto.setIds(postId, commentId, loginMemberId);

        commentService.updateComment(commentDto);
    }

    /**
     * 댓글(답글) 삭제
     */
    @DeleteMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable("commentId") Long commentId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        commentService.deleteComment(commentId, loginMemberId);
    }

}
