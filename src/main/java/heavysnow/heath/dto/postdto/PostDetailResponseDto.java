package heavysnow.heath.dto.postdto;

import heavysnow.heath.domain.Comment;
import heavysnow.heath.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostDetailResponseDto {
    private Long memberId;
    private String profileImgUrl;
    private String nickname;
    private Long postId;
    private String title;
    private String content;
    private LocalDate createdDate;
    private int consecutiveDays;
    private int liked;
    private boolean isLiked;
    private List<PostImageInfo> imgs;
    private List<CommentResponseDto> comments;

    public boolean isLiked() {
        return isLiked;
    }

    public static PostDetailResponseDto of(Post post, Long memberId, List<Comment> parentComments) {
        return new PostDetailResponseDto(
                post.getMember().getId(),
                post.getMember().getProfileImgUrl(),
                post.getMember().getNickname(),
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedDate().toLocalDate(),
                post.getConsecutiveDays(),
                post.getMemberPostLikedList().size(),
                isMemberPostLiked(post, memberId),
                PostImageInfo.listOf(post.getPostImages()),
                CommentResponseDto.listOf(parentComments)
        );
    }

    private static boolean isMemberPostLiked(Post post, Long memberId) {
        return post.getMemberPostLikedList().stream().anyMatch(mpl -> Objects.equals(mpl.getMember().getId(), memberId));
    }
}
