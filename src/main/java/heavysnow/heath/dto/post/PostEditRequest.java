package heavysnow.heath.dto.post;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class PostEditRequest {
    private Long postId;
    private String title;
    private String content;
    private List<String> postImages;
}
