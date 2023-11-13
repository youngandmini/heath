package heavysnow.heath.service;


import heavysnow.heath.domain.Member;
import heavysnow.heath.domain.MemberPostLiked;
import heavysnow.heath.domain.Post;
import heavysnow.heath.exception.NotFoundException;
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
     * 특정 멤버가 특정 게시글에 좋아요를 추가하거나 취소
     * @param postId: 해당 게시글에
     * @param loginMemberId: 해당 멤버가 좋아요 기능을 이용
     */
    public void changeMemberPostLiked(Long postId, Long loginMemberId) {
        Post findPost = postRepository.findById(postId).orElseThrow(NotFoundException::new);
        Member findMember = memberRepository.findById(loginMemberId).orElseThrow();

        Optional<MemberPostLiked> memberPostLikedOptional = memberPostLikedRepository.findByMemberIdAndPostId(loginMemberId, postId);
        if (memberPostLikedOptional.isPresent()) {
            memberPostLikedRepository.delete(memberPostLikedOptional.get());
        } else {
            MemberPostLiked memberPostLiked = new MemberPostLiked(findMember, findPost);
            memberPostLikedRepository.save(memberPostLiked);
        }
    }
}
