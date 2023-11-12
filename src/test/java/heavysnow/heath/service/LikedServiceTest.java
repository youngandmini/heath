package heavysnow.heath.service;

import heavysnow.heath.domain.MemberPostLiked;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.dto.postdto.PostDetailResponseDto;
import heavysnow.heath.repository.MemberPostLikedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class LikedServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;
    @Autowired
    LikedService likedService;
    @Autowired
    MemberPostLikedRepository memberPostLikedRepository;

    @Test
    void changeMemberPostLiked() {
        //given
        MemberDto memberDto = new MemberDto("memner1@example.com", "nickname", "", "");
        Long savedMemberId = memberService.createUser(memberDto);
        List<String> imgUrls = new ArrayList<>();
        imgUrls.add("이미지1");
        PostAddRequest addRequest = new PostAddRequest(savedMemberId, "제목1", "내용1", imgUrls);
        Long savedPostId = postService.writePost(addRequest).getPostId();

        //when - then -> 좋아요를 한번 누르면 좋아요가 반영된다.
        likedService.changeMemberPostLiked(savedPostId, savedMemberId);
        PostDetailResponseDto responseDto1 = postService.getPostWithDetail(savedPostId, savedMemberId);
        Optional<MemberPostLiked> memberPostLikedOptional1 = memberPostLikedRepository.findByMemberIdAndPostId(savedMemberId, savedPostId);

        assertThat(memberPostLikedOptional1).isNotEmpty();
        assertThat(responseDto1.isLiked()).isTrue();

        //when - then -> 이미 좋아요가 눌린 상태에서 한번 더 누르면 좋아요가 취소된다.
        likedService.changeMemberPostLiked(savedPostId, savedMemberId);
        PostDetailResponseDto responseDto2 = postService.getPostWithDetail(savedPostId, savedMemberId);
        Optional<MemberPostLiked> memberPostLikedOptional2 = memberPostLikedRepository.findByMemberIdAndPostId(savedMemberId, savedPostId);

        assertThat(memberPostLikedOptional2).isEmpty();
        assertThat(responseDto2.isLiked()).isFalse();
    }
}