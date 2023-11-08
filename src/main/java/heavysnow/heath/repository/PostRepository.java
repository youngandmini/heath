package heavysnow.heath.repository;

import heavysnow.heath.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {


    //MySQL 버전
//    @Query("select p from Post p where date_format(p.createdDate, '%Y-%m-%d') = :yesterday order by p.id limit 1")
    //H2 버전
    @Query("SELECT p FROM Post p WHERE FORMATDATETIME(p.createdDate, 'yyyy-MM-dd') = :yesterday order by p.id limit 1")
    Optional<Post> findByCreatedDate(@Param("yesterday") LocalDate yesterday);


    @Query("select p from Post p join fetch p.member m where p.member.id = :memberId order by p.createdDate desc")
    Slice<Post> findPageByMember(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select p from Post p join fetch p.member m")
    Slice<Post> findPage(Pageable pageable);


    /**
     * use lazy loading on postImages column
     */
    @Query("select distinct p from Post p" +
            " left join fetch p.member" +
//            " left join fetch p.postImages" +
            " left join fetch p.memberPostLikedList" +
            " where p.id = :postId")
    Optional<Post> findPostDetailById(@Param("postId") Long postId);

}
