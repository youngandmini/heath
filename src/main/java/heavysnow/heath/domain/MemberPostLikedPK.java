package heavysnow.heath.domain;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberPostLikedPK implements Serializable {

    private Long member;
    private Long post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberPostLikedPK that = (MemberPostLikedPK) o;
        return Objects.equals(getMember(), that.getMember()) && Objects.equals(getPost(), that.getPost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMember(), getPost());
    }
}
