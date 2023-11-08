package heavysnow.heath.domain;


import heavysnow.heath.dto.CommentCreateDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    private List<Comment> childComments;


    // 빌더 패턴으로 엔티티 생성 메서드
    public static Comment createComment(Post post, Member member, String content, Comment parentComment) {
        return Comment.builder()
                .post(post)
                .member(member)
                .content(content)
                .parentComment(parentComment)
                .build();
    }
}
