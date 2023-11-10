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

    public void setIds(Long postId, Long commentId, Long memberId) {
        this.postId = postId;
        this.commentId = commentId;
        this.memberId = memberId;
    }
}
