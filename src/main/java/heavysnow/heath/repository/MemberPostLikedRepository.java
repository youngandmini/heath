package heavysnow.heath.repository;

import heavysnow.heath.domain.Member;
import heavysnow.heath.domain.MemberPostLiked;
import heavysnow.heath.domain.MemberPostLikedPK;
import heavysnow.heath.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberPostLikedRepository extends JpaRepository<MemberPostLiked, MemberPostLikedPK> {

    Optional<MemberPostLiked> findByMemberIdAndPostId(@Param("memberId") Long memberId, @Param("postId") Long postId);
}
