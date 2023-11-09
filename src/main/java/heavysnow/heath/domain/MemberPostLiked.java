package heavysnow.heath.domain;


import jakarta.persistence.*;

@Entity
@IdClass(MemberPostLikedPK.class)
@Table(name = "member_post_liked")
public class MemberPostLiked {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Id
    @Column(name = "post_id")
    private Long postId;

}
