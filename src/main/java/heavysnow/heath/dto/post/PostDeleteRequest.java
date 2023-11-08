package heavysnow.heath.dto.post;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class PostDeleteRequest {
    private Long postId;
}