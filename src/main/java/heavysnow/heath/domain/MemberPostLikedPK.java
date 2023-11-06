package heavysnow.heath.domain;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberPostLikedPK implements Serializable {

    private Long member;
    private Long post;
}
