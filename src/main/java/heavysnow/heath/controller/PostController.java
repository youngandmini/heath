package heavysnow.heath.controller;

import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.dto.CommentCreateDto;
import heavysnow.heath.dto.postdto.PostDetailResponseDto;
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
    public Long addComment(@RequestBody CommentCreateDto commentDto, @PathVariable("postId") Long postId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        commentDto.setPostId(postId);
        commentDto.setMemberId(loginMemberId);
        Long savedCommentId = commentService.createComment(commentDto);
        return savedCommentId;
    }

    /**
     * 답글 요청
     */
    @PostMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Long addReply(@RequestBody CommentCreateDto commentDto, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(request.getHeader("accessToken"));
        Long loginMemberId = loginMemberIdOptional.orElseThrow(UnauthorizedException::new);

        commentDto.setPostId(postId);
        commentDto.setMemberId(loginMemberId);
        commentDto.setParentCommentId(commentId);
        Long savedCommentId = commentService.createComment(commentDto);
        return savedCommentId;
    }

}
