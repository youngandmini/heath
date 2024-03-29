package heavysnow.heath.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String title;
    private String content;
//    private int liked;
    private int consecutiveDays;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_image_id")
    private PostImage mainImage;

    @OneToMany(mappedBy = "post")
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
  
    @OneToMany(mappedBy = "post")
    private List<MemberPostLiked> memberPostLikedList = new ArrayList<>();

    public Post(Member member, String title, String content, int consecutiveDays) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.consecutiveDays = consecutiveDays;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setMainImage(PostImage mainImage) {
        this.mainImage = mainImage;
    }
}
