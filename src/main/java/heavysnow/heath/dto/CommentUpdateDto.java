package heavysnow.heath.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentUpdateDto {
    private Long postId;
    private Long memberId;
    private Long commentId;
    private String content;

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
