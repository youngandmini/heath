package heavysnow.heath.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdClass(MemberPostLikedPK.class)
@Table(name = "member_post_liked")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public MemberPostLiked(Member member, Post post) {
        this.member = member;
        this.post = post;
//        post.getMemberPostLikedList().add(this);
    }
}
