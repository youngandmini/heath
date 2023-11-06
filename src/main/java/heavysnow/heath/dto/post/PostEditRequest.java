package heavysnow.heath.dto.post;

import heavysnow.heath.domain.PostImage;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.NONE)
public class PostEditRequest {
    private Long postId;
    private String title;
    private String content;
    private List<String> images;
    private List<PostImage> postImages;
}
