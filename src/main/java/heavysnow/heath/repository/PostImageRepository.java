package heavysnow.heath.repository;

import heavysnow.heath.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    /**
     * 게시글 이미지들을 삭제
     * @param postImages: 해당 게시글 이미지들을 삭제
     */
    @Modifying
    @Query("DELETE FROM PostImage postImage WHERE postImage IN :postImages")
    void deletePostImages(@Param("postImages") List<PostImage> postImages);

    /**
     * 해당 게시글의 이미지들을 전부 삭제
     * @param postId: 해당 게시글의 이미지들을 전부 삭제
     */
    @Modifying
    @Query("DELETE FROM PostImage pi WHERE pi.post.id=:postId")
    void deletePostImagesByPostId(@Param("postId") Long postId);


    /**
     * 특정 url을 갖는 게시글 이미지를 탐색
     * @param url: 해당 url을 갖는 게시글 이미지를 탐색
     * @return: 게시글 이미지를 반환
     */
    @Query("SELECT pi FROM PostImage pi WHERE pi.imgUrl = :url")
    Optional<PostImage> findByUrl(@Param("url") String url);
}
