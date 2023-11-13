package heavysnow.heath.dto.postdto;

import heavysnow.heath.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostListResponse {

    private List<PostInfo> posts;
    private PageInfo pageInfo;

    public static PostListResponse of(Slice<Post> postSlice) {
        return new PostListResponse(PostInfo.listOf(postSlice), PageInfo.of(postSlice));
    }
}
