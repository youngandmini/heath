package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.domain.Post;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.dto.post.PostEditRequest;
import heavysnow.heath.dto.postdto.PostDetailResponseDto;
import heavysnow.heath.dto.postdto.PostListResponseDto;
import heavysnow.heath.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Test
    void getPostListByMember() {
        //given
        MemberDto memberDto = new MemberDto("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberDto);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        List<String> imgUrls2 = new ArrayList<>();
        imgUrls2.add("이미지21");
        imgUrls2.add("이미지22");
        imgUrls2.add("이미지23");

        PostAddRequest postAddRequest1 = new PostAddRequest(memberId, "게시글 제목1", "게시글 내용1", imgUrls1);
        PostAddRequest postAddRequest2 = new PostAddRequest(memberId, "게시글 제목2", "게시글 내용2", imgUrls2);
        Long savedPostId1 = postService.writePost(postAddRequest1);
        Long savedPostId2 = postService.writePost(postAddRequest2);

        //when - 멤버별로 최신순으로 검색한다.
        PostListResponseDto responseDto = postService.getPostListByMember(memberId, 0);

        //then
        assertThat(responseDto.getPageInfo().getNumberOfElements()).isEqualTo(2);
        assertThat(responseDto.getPosts().get(0).getPostId()).isEqualTo(savedPostId2);
        assertThat(responseDto.getPosts().get(0).getMainImgUrl()).isEqualTo("이미지21");
        assertThat(responseDto.getPosts().get(1).getPostId()).isEqualTo(savedPostId1);
        assertThat(responseDto.getPosts().get(1).getMainImgUrl()).isEqualTo("이미지1");
    }

    @Test
    void getPostList() {
        //given
        MemberDto memberDto = new MemberDto("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberDto);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        List<String> imgUrls2 = new ArrayList<>();
        imgUrls2.add("이미지21");
        imgUrls2.add("이미지22");
        imgUrls2.add("이미지23");

        PostAddRequest postAddRequest1 = new PostAddRequest(memberId, "게시글 제목1", "게시글 내용1", imgUrls1);
        PostAddRequest postAddRequest2 = new PostAddRequest(memberId, "게시글 제목2", "게시글 내용2", imgUrls2);
        Long savedPostId1 = postService.writePost(postAddRequest1);
        Long savedPostId2 = postService.writePost(postAddRequest2);

        //when
        PostListResponseDto responseDto = postService.getPostList(0, "createdDate");

        //then
        assertThat(responseDto.getPosts().get(0).getPostId()).isEqualTo(savedPostId2);
        assertThat(responseDto.getPosts().get(1).getPostId()).isEqualTo(savedPostId1);
        assertThat(responseDto.getPageInfo().getNumberOfElements()).isEqualTo(2);

    }

    @Test
    void getPostWithDetail() {
        //given
        MemberDto memberDto = new MemberDto("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberDto);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        List<String> imgUrls2 = new ArrayList<>();
        imgUrls2.add("이미지21");
        imgUrls2.add("이미지22");
        imgUrls2.add("이미지23");

        PostAddRequest postAddRequest1 = new PostAddRequest(memberId, "게시글 제목1", "게시글 내용1", imgUrls1);
        PostAddRequest postAddRequest2 = new PostAddRequest(memberId, "게시글 제목2", "게시글 내용2", imgUrls2);
        Long savedPostId1 = postService.writePost(postAddRequest1);
        Long savedPostId2 = postService.writePost(postAddRequest2);

        //when
        PostDetailResponseDto responseDto = postService.getPostWithDetail(savedPostId1, memberId);

        //then
        assertThat(responseDto.getPostId()).isEqualTo(savedPostId1);
        assertThat(responseDto.getMemberId()).isEqualTo(memberId);
        assertThat(responseDto.getTitle()).isEqualTo("게시글 제목1");
        assertThat(responseDto.getContent()).isEqualTo("게시글 내용1");
        assertThat(responseDto.getLiked()).isEqualTo(0);
        assertThat(responseDto.getConsecutiveDays()).isEqualTo(1);
        assertThat(responseDto.getImgs().size()).isEqualTo(3);
        assertThat(responseDto.getImgs().get(0).getImgUrl()).isEqualTo("이미지1");
        assertThat(responseDto.isLiked()).isFalse();

    }


    @Test
    void postUpdateTest() {
        //given
        MemberDto memberDto = new MemberDto("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberDto);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        PostAddRequest postAddRequest1 = new PostAddRequest(memberId, "게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(postAddRequest1);

        //when
        List<String> editImgUrls = new ArrayList<>();
        editImgUrls.add("수정 이미지1");
        editImgUrls.add("수정 이미지2");
        editImgUrls.add("수정 이미지3");
        editImgUrls.add("수정 이미지4");
        PostEditRequest editRequest = new PostEditRequest(savedPostId1, "수정 제목1", "수정 게시글 내용1", editImgUrls);
        postService.editPost(editRequest);
        PostDetailResponseDto responseDto = postService.getPostWithDetail(savedPostId1, memberId);

        //then
        assertThat(responseDto.getPostId()).isEqualTo(savedPostId1);
        assertThat(responseDto.getMemberId()).isEqualTo(memberId);
        assertThat(responseDto.getTitle()).isEqualTo("수정 제목1");
        assertThat(responseDto.getContent()).isEqualTo("수정 게시글 내용1");
        assertThat(responseDto.getLiked()).isEqualTo(0);
        assertThat(responseDto.getConsecutiveDays()).isEqualTo(1);
        assertThat(responseDto.getImgs().size()).isEqualTo(4);
        assertThat(responseDto.getImgs().get(0).getImgUrl()).isEqualTo("수정 이미지1");
        assertThat(responseDto.getImgs().get(1).getImgUrl()).isEqualTo("수정 이미지2");
        assertThat(responseDto.getImgs().get(2).getImgUrl()).isEqualTo("수정 이미지3");
        assertThat(responseDto.getImgs().get(3).getImgUrl()).isEqualTo("수정 이미지4");

    }

    @Test
    void postDeleteTest() {
        //given
        MemberDto memberDto = new MemberDto("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberDto);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        PostAddRequest postAddRequest1 = new PostAddRequest(memberId, "게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(postAddRequest1);

        //when
        postService.deletePost(savedPostId1, memberId);

        //then
        assertThatThrownBy(() -> postService.getPostWithDetail(savedPostId1, memberId))
                .isInstanceOf(NoSuchElementException.class);
    }

}