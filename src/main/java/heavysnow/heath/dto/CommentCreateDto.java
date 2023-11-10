package heavysnow.heath.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class CommentCreateDto {
    private Long postId;
    private Long memberId;
    private String content;
    private Long parentCommentId;   // 대댓글인 경우 부모 댓글의 ID

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
