package heavysnow.heath.repository;

import heavysnow.heath.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    /**
     * lazy loading on child
     * @param postId
     * @return
     */
    @Query("select distinct c from Comment c" +
            " left join fetch c.member" +
            " left join fetch c.childComments cc" +
            " left join fetch cc.member" +
            " where c.post.id = :postId" +
            " and c.parentComment is null")
    List<Comment> findWithMemberByPostId(@Param("postId") Long postId);
}
