package heavysnow.heath.service;

import heavysnow.heath.domain.Comment;
import heavysnow.heath.domain.Member;
import heavysnow.heath.domain.Post;
import heavysnow.heath.dto.comment.CommentCreateRequest;
import heavysnow.heath.dto.comment.CommentCreateResponse;
import heavysnow.heath.dto.comment.CommentUpdateRequest;
import heavysnow.heath.exception.BadRequestException;
import heavysnow.heath.exception.ForbiddenException;
import heavysnow.heath.exception.NotFoundException;
import heavysnow.heath.repository.CommentRepository;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * 생성 : 맴버의 게시글에 대한 댓글 생성하는 메서드
     * @param commentCreateRequest
     * @return
     */
    public CommentCreateResponse createComment(Long postId, CommentCreateRequest commentCreateRequest, Long parentCommentId, Long loginMemberId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundException::new);

        Member member = memberRepository.findById(loginMemberId).orElseThrow();

        Comment parentComment = null;
        if (parentCommentId != null) {
            parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(NotFoundException::new);
        }
        Comment comment = Comment.createComment(post, member, commentCreateRequest.getContent(), parentComment);
        Comment savedcomment = commentRepository.save(comment);

        return CommentCreateResponse.of(savedcomment.getId());
    }

    public void updateComment(Long postId, Long commentId, CommentUpdateRequest commentUpdateRequest, Long loginMemberId) {
        // 댓글 존재 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundException::new);

        // 댓글이 해당 멤버와 포스트에 속하는지 확인
        if (!comment.getPost().getId().equals(postId)) {
            throw new BadRequestException();
        }
        if (!comment.getMember().getId().equals(loginMemberId)) {
            throw new ForbiddenException();
        }

        // 내용 업데이트
        comment.updateComment(commentUpdateRequest.getContent());
    }

    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundException::new);

        if (!comment.getMember().getId().equals(memberId)) {
            throw new ForbiddenException();
        }
        commentRepository.delete(comment);
    }
}
