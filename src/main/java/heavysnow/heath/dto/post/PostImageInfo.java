package heavysnow.heath.dto.post;

import heavysnow.heath.domain.PostImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostImageInfo {

    private String imgUrl;

    public static List<PostImageInfo> listOf(List<PostImage> postImages) {
        return postImages.stream().map(PostImageInfo::of).collect(Collectors.toList());
    }

    private static PostImageInfo of(PostImage postImage) {
        return new PostImageInfo(postImage.getImgUrl());
    }
}
