package heavysnow.heath.dto.postdto;

import heavysnow.heath.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


/**
 * MemberPostListResponseDto에 들어갈 List<Post> 정보를 위한 클래스
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberPostInfo {

    private Long postId;
    private String title;
    private String content;
    private String mainImgUrl;
    private LocalDate createdDate;
    private int consecutiveDays;

    public static List<MemberPostInfo> listOf(Page<Post> postPage) {
        return postPage.getContent().stream().map(MemberPostInfo::of).collect(Collectors.toList());
    }

    private static MemberPostInfo of(Post post) {
        return new MemberPostInfo(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMainImage().getImgUrl(),
                post.getCreatedDate().toLocalDate(),
                post.getConsecutiveDays());
    }
}
