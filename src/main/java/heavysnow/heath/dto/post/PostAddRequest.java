package heavysnow.heath.dto.post;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.NONE)
public class PostAddRequest {
    private Long memberId;
    private String title;
    private String content;
    private List<String> images;
}
