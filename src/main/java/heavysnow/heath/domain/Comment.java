package heavysnow.heath.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComments = new ArrayList<>();


    // 빌더 패턴으로 엔티티 생성 메서드
    public static Comment createComment(Post post, Member member, String content, Comment parentComment) {
        return Comment.builder()
                .post(post)
                .member(member)
                .content(content)
                .parentComment(parentComment)
                .build();
    }

    public void updateComment(String newComment) {
        this.content = newComment;
    }

    @Builder
    public Comment(Long id, Post post, Member member, String content, Comment parentComment) {
        this.id = id;
        this.post = post;
        this.member = member;
        this.content = content;
        this.parentComment = parentComment;
        if (parentComment != null) {
            parentComment.getChildComments().add(this);
        }
    }
}
