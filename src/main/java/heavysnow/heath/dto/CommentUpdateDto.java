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
    private String newComment;
}
