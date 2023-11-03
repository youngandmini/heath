package heavysnow.heath.dto.postdto;

import heavysnow.heath.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;


/**
 * 페이지네이션 정보를 위한 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PageInfo {
    private boolean first;
    private boolean last;
    private int page;
    private int numberOfElements;

    public static PageInfo of(Page<Post> postPage) {
        return new PageInfo(postPage.isFirst(), postPage.isLast(), postPage.getNumber(), postPage.getNumberOfElements());
    }
}
