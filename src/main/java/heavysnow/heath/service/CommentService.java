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
     * 게시글에 대한 댓글을 생성하는 메서드
     * @param postId: 해당 게시글에
     * @param commentCreateRequest: 이 내용으로
     * @param parentCommentId: 이 댓글 하위에
     * @param loginMemberId: 이 멤버가 댓글을 등록한다.
     * @return: 댓글의 id를 반환
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

    /**
     * 게시글에 대한 댓글을 수정하는 메서드
     * @param postId: 해당 게시글에
     * @param commentId: 해당 댓글을
     * @param commentUpdateRequest: 이 내용으로
     * @param loginMemberId: 이 멤버가 댓글을 수정한다.
     */
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

    /**
     * 댓글을 삭제하는 메서드
     * @param commentId: 해당 댓글에 대해
     * @param loginMemberId: 해당 멤버가 댓글을 삭제한다.
     */
    public void deleteComment(Long commentId, Long loginMemberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundException::new);

        if (!comment.getMember().getId().equals(loginMemberId)) {
            throw new ForbiddenException();
        }
        commentRepository.delete(comment);
    }
}
