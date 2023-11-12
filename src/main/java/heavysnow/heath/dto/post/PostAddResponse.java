package heavysnow.heath.dto.post;

import heavysnow.heath.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostAddResponse {
    private Long postId;

    public static PostAddResponse of(Post post) {
        return new PostAddResponse(post.getId());
    }
}
