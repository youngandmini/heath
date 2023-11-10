package heavysnow.heath.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommentCreateResponseDto {

    private Long commentId;

    public static CommentCreateResponseDto of(Long commentId) {
        return new CommentCreateResponseDto(commentId);
    }
}
