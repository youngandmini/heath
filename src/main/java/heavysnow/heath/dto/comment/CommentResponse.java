package heavysnow.heath.dto.comment;

import heavysnow.heath.domain.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommentResponse {

    private Long memberId;
    private String profileImgUrl;
    private String nickname;
    private Long commentId;
    private String content;
    private LocalDate createdDate;
    private List<ChildCommentInfo> childComments;


    public static List<CommentResponse> listOf(List<Comment> parentComments) {
        return parentComments.stream().map(CommentResponse::of).collect(Collectors.toList());
    }

    private static CommentResponse of(Comment parentComment) {
        return new CommentResponse(
                parentComment.getMember().getId(),
                parentComment.getMember().getProfileImgUrl(),
                parentComment.getMember().getNickname(),
                parentComment.getId(),
                parentComment.getContent(),
                parentComment.getCreatedDate().toLocalDate(),
                ChildCommentInfo.listOf(parentComment.getChildComments())
        );
    }
}
