package heavysnow.heath.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_image")
@Getter
public class PostImage {

    @Id
    @GeneratedValue
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String imgUrl;

    public PostImage(Post post, String imgUrl) {
        this.post = post;
        this.imgUrl = imgUrl;
        post.getPostImages().add(this);
    }

}
