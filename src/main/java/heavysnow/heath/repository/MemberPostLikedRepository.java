package heavysnow.heath.repository;

import heavysnow.heath.domain.MemberPostLiked;
import heavysnow.heath.domain.MemberPostLikedPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberPostLikedRepository extends JpaRepository<MemberPostLiked, MemberPostLikedPK> {

    /**
     * 해당 멤버가 해당 게시글에 좋아요를 눌렀는지를 확인
     * @param memberId: 해당 멤버가
     * @param postId: 해당 게시글에 좋아요를 눌렀는지 확인
     * @return: 좋아요 레코드를 반환
     */
    Optional<MemberPostLiked> findByMemberIdAndPostId(@Param("memberId") Long memberId, @Param("postId") Long postId);
}
