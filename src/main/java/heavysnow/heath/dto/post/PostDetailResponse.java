package heavysnow.heath.dto.post;

import heavysnow.heath.domain.Comment;
import heavysnow.heath.domain.Post;
import heavysnow.heath.domain.PostImage;
import heavysnow.heath.dto.comment.CommentResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostDetailResponse {
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
    private List<String> postImgUrls;
    private List<CommentResponse> comments;

    public boolean isLiked() {
        return isLiked;
    }

    public static PostDetailResponse of(Post post, Long loginMemberId, List<Comment> parentComments) {
        return new PostDetailResponse(
                post.getMember().getId(),
                post.getMember().getProfileImgUrl(),
                post.getMember().getNickname(),
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedDate().toLocalDate(),
                post.getConsecutiveDays(),
                post.getMemberPostLikedList().size(),
                isMemberPostLiked(post, loginMemberId),
                post.getPostImages().stream().map(PostImage::getImgUrl).collect(Collectors.toList()),
//                PostImageInfo.listOf(post.getPostImages()),
                CommentResponse.listOf(parentComments)
        );
    }

    private static boolean isMemberPostLiked(Post post, Long loginMemberId) {
        if (loginMemberId == null) {
            return false;
        }
        return post.getMemberPostLikedList().stream().anyMatch(mpl -> Objects.equals(mpl.getMember().getId(), loginMemberId));
    }
}
