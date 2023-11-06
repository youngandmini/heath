package heavysnow.heath.dto.postdto;

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
public class ChildCommentInfo {

    private Long memberId;
    private String profileImgUrl;
    private String nickname;
    private Long commentId;
    private String content;
    private LocalDate createdDate;


    public static List<ChildCommentInfo> listOf(List<Comment> childComments) {
        return childComments.stream().map(ChildCommentInfo::of).collect(Collectors.toList());
    }

    private static ChildCommentInfo of(Comment childComment) {
        return new ChildCommentInfo(
                childComment.getMember().getId(),
                childComment.getMember().getProfileImgUrl(),
                childComment.getMember().getNickname(),
                childComment.getId(),
                childComment.getContent(),
                childComment.getCreatedDate().toLocalDate()
        );
    }
}
