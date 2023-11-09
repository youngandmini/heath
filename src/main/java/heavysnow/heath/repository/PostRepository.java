package heavysnow.heath.repository;

import heavysnow.heath.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {


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


    //MySQL 버전
//    @Query("select distinct p.createdDate from Post p" +
//            " where p.member.id = :memberId" +
//            " and date_format(p.createdDate, '%y') = :year" +
//            " and date_format(p.createdDate, '%m') = :month" +
//            " order by p.createdDate")
    //H2database 버전
    @Query("select distinct p.createdDate from Post p" +
            " where p.member.id = :memberId" +
            " and FORMATDATETIME(p.createdDate, 'yyyy') = :year" +
            " and FORMATDATETIME(p.createdDate, 'MM') = :month" +
            " order by p.createdDate")
    List<LocalDateTime> findDatesByMemberAndYearMonth(@Param("memberId") Long memberId, @Param("year") int year, @Param("month") int month);
}
