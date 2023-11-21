package heavysnow.heath.dto.post;

import heavysnow.heath.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


/**
 * PostListResponseDto에 들어갈 List<Post> 정보를 위한 클래스
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostInfo {

    private Long memberId;
    private Long postId;
    private String profileImgUrl;
    private String nickname;
    private String title;
    private String content;
    private String mainImgUrl;
    private LocalDate createdDate;
    private int consecutiveDays;

    public static List<PostInfo> listOf(Slice<Post> postSlice) {
        return postSlice.getContent().stream().map(PostInfo::of).collect(Collectors.toList());
    }

    private static PostInfo of(Post post) {
        return new PostInfo(
                post.getMember().getId(),
                post.getId(),
                post.getMember().getProfileImgUrl(),
                post.getMember().getNickname(),
                post.getTitle(),
                post.getContent(),
                post.getMainImage().getImgUrl(),
                post.getCreatedDate().toLocalDate(),
                post.getConsecutiveDays());
    }
}
