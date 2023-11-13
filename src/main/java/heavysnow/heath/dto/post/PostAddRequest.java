package heavysnow.heath.dto.post;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class PostAddRequest {
    private String title;
    private String content;
    private List<String> postImgUrls;

}
