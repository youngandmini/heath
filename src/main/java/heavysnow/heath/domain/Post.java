package heavysnow.heath.domain;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String title;
    private String content;
    private int liked;
    private int consecutiveDays;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_image_id")
    private PostImage mainImage;

    @OneToMany(mappedBy = "post")
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
}
