package heavysnow.heath.service;


import heavysnow.heath.domain.Member;
import heavysnow.heath.domain.MemberPostLiked;
import heavysnow.heath.domain.Post;
import heavysnow.heath.repository.MemberPostLikedRepository;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikedService {
    private final MemberPostLikedRepository memberPostLikedRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    /**
     * memberId를 가진 멤버가 postId를 가진 게시글에 좋아요를 누르거나 취소하는 상황
     * @param postId
     * @param memberId
     */
    public void changeMemberPostLiked(Long postId, Long memberId) {
        Post findPost = postRepository.findById(postId).orElseThrow();
        Member findMember = memberRepository.findById(memberId).orElseThrow();

        Optional<MemberPostLiked> memberPostLikedOptional = memberPostLikedRepository.findByMemberIdAndPostId(memberId, postId);
        if (memberPostLikedOptional.isPresent()) {
            memberPostLikedRepository.delete(memberPostLikedOptional.get());
        } else {
            MemberPostLiked memberPostLiked = new MemberPostLiked(findMember, findPost);
            memberPostLikedRepository.save(memberPostLiked);
        }
    }
}
