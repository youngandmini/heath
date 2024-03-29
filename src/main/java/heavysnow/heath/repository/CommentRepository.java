package heavysnow.heath.repository;

import heavysnow.heath.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 게시글의 댓글과 답글을 엮어서 조회
     * @param postId: 해당 게시글의 댓글과 답글을 조회
     * @return: 댓글 리스트를 반환
     */
    @Query("select distinct c from Comment c" +
            " left join fetch c.member" +
            " left join fetch c.childComments cc" +
            " left join fetch cc.member" +
            " where c.post.id = :postId" +
            " and c.parentComment is null")
    List<Comment> findWithMemberByPostId(@Param("postId") Long postId);
}
