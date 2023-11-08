package heavysnow.heath.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@IdClass(MemberPostLikedPK.class)
@Table(name = "member_post_liked")
@Getter
public class MemberPostLiked {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

}
