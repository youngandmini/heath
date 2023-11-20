package heavysnow.heath.repository;

import heavysnow.heath.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 특정 회원이 특정 날짜에 저장한 게시글을 조회
     * @param memberId: 해당 회원이
     * @param yesterday: 해당 날짜에 저장한 게시글을 조회
     * @return: 게시글을 반환
     */
    @Query("select p from Post p where p.member.id = :memberId and date_format(p.createdDate, '%Y-%m-%d') = :yesterday order by p.id limit 1")
    Optional<Post> findByCreatedDate(@Param("memberId") Long memberId, @Param("yesterday") LocalDate yesterday);


    /**
     * 특정 회원의 게시글을 등록일로 내림차순 정렬하여 조회
     * @param memberId: 특정 회원의 게시글을
     * @param pageable: 해당 페이징 정보로 조회
     * @return: 게시글의 슬라이스를 반환
     */
    @Query("select p from Post p join fetch p.member m where p.member.id = :memberId order by p.createdDate desc")
    Slice<Post> findPageByMember(@Param("memberId") Long memberId, Pageable pageable);

    /**
     * 게시글을 최신순으로 페이징하여 조회
     * @param pageable: 해당 페이징 정보로 페이징하여 조회
     * @return: 게시글의 슬라이스를 반환
     */
    @Query("select p from Post p join fetch p.member m order by p.createdDate desc ")
    Slice<Post> findPageOrderByCreatedDate(Pageable pageable);

    /**
     * 게시글을 좋아요순으로 페이징하여 조회
     * @param pageable: 해당 페이징 정보로 페이징하여 조회
     * @return: 게시글의 슬라이스를 반환
     */
    @Query("select p from Post p" +
            " join fetch p.member m" +
            " left outer join p.memberPostLikedList mpl" +
            " on p.id = mpl.post.id" +
            " group by p.id" +
            " order by count(mpl.member.id) desc ")
    Slice<Post> findPageOrderByLiked(Pageable pageable);


    /**
     * 특정 게시글의 상세정보를 조회 (게시글 이미지에는 지연로딩 적용)
     * @param postId: 해당 게시글의 상세정보를 조회
     * @return: 게시글 반환
     */
    @Query("select distinct p from Post p" +
            " left join fetch p.member" +
//            " left join fetch p.postImages" +
            " left join fetch p.memberPostLikedList" +
            " where p.id = :postId")
    Optional<Post> findPostDetailById(@Param("postId") Long postId);

    /**
     * 특정 회원이 특정 기간동안 등록한 게시글의 날짜를 조회
     * @param memberId: 해당 회원이
     * @param year: 해당 년
     * @param month: 해당 월에 등록했던 게시글들의 날짜를 조회
     * @return: 날짜 리스트를 반환
     */
    @Query("select distinct p.createdDate from Post p" +
            " where p.member.id = :memberId" +
            " and date_format(p.createdDate, '%Y') = :year" +
            " and date_format(p.createdDate, '%m') = :month" +
            " order by p.createdDate")
    List<LocalDateTime> findDatesByMemberAndYearMonth(@Param("memberId") Long memberId, @Param("year") int year, @Param("month") int month);
}
