package heavysnow.heath.repository;

import heavysnow.heath.domain.PostImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    @Modifying()
    @Transactional
    @Query("DELETE FROM PostImage postImage WHERE postImage IN :postImages")
    void deletePostImages(@Param("postImages") List<PostImage> postImages);

    @Modifying
    @Query("DELETE FROM PostImage pi WHERE pi.post.id=:postId")
    void deletePostImagesByPostId(@Param("postId") Long postId);

    @Query("SELECT PI FROM PostImage PI WHERE PI.imgUrl = :url")
    PostImage findByUrl(@Param("url") String url);
}
