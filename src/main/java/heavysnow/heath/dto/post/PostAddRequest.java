package heavysnow.heath.dto.post;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Valid
    @NotNull
    @Size(min = 1, max = 1)
    private List<String> postImgUrls;

}
