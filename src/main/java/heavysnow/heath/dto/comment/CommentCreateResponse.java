package heavysnow.heath.dto.comment;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommentCreateResponse {

    private Long commentId;

    public static CommentCreateResponse of(Long commentId) {
        return new CommentCreateResponse(commentId);
    }
}
