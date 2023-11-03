package heavysnow.heath.domain;


import jakarta.persistence.*;

@Entity
@IdClass(MemberPostLikedPK.class)
@Table(name = "member_post_liked")
public class MemberPostLiked {

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
