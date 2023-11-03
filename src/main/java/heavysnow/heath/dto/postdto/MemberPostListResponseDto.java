package heavysnow.heath.dto.postdto;

import heavysnow.heath.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberPostListResponseDto {

    private List<MemberPostInfo> posts;
    private PageInfo pageInfo;

    public static MemberPostListResponseDto of(Page<Post> postPage) {
        return new MemberPostListResponseDto(MemberPostInfo.listOf(postPage), PageInfo.of(postPage));
    }
}
