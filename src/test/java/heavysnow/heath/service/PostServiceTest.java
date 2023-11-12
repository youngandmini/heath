package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.domain.Post;
import heavysnow.heath.dto.CommentCreateDto;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.dto.post.PostEditRequest;
import heavysnow.heath.dto.postdto.PostDetailResponseDto;
import heavysnow.heath.dto.postdto.PostListResponseDto;
import heavysnow.heath.exception.NotFoundException;
import heavysnow.heath.repository.CommentRepository;
import heavysnow.heath.repository.MemberPostLikedRepository;
import heavysnow.heath.repository.MemberRepository;
import jakarta.persistence.EntityManager;
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
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    EntityManager em;
    @Autowired
    LikedService likedService;
    @Autowired
    MemberPostLikedRepository memberPostLikedRepository;


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
        Long savedPostId1 = postService.writePost(postAddRequest1).getPostId();
        Long savedPostId2 = postService.writePost(postAddRequest2).getPostId();

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
        Long savedPostId1 = postService.writePost(postAddRequest1).getPostId();
        Long savedPostId2 = postService.writePost(postAddRequest2).getPostId();

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
        Long savedPostId1 = postService.writePost(postAddRequest1).getPostId();
        Long savedPostId2 = postService.writePost(postAddRequest2).getPostId();

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
        Long savedPostId1 = postService.writePost(postAddRequest1).getPostId();

        //when
        List<String> editImgUrls = new ArrayList<>();
        editImgUrls.add("수정 이미지1");
        editImgUrls.add("수정 이미지2");
        editImgUrls.add("수정 이미지3");
        editImgUrls.add("수정 이미지4");
        PostEditRequest editRequest = new PostEditRequest(savedPostId1, "수정 제목1", "수정 게시글 내용1", editImgUrls);
        postService.editPost(editRequest, memberId);
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
        Long savedPostId1 = postService.writePost(postAddRequest1).getPostId();

        //when
        postService.deletePost(savedPostId1, memberId);

        //then
        assertThatThrownBy(() -> postService.getPostWithDetail(savedPostId1, memberId))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    void postDeleteWithCommentsTest() {
        //given
        MemberDto memberDto = new MemberDto("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberDto);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");

        PostAddRequest postAddRequest1 = new PostAddRequest(memberId, "게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(postAddRequest1).getPostId();

        //댓글 달기
        CommentCreateDto commentDto1 = new CommentCreateDto(savedPostId1, memberId, "댓글1", null);
        CommentCreateDto commentDto2 = new CommentCreateDto(savedPostId1, memberId, "댓글2", null);
        Long savedCommentId1 = commentService.createComment(commentDto1).getCommentId();
        Long savedCommentId2 = commentService.createComment(commentDto2).getCommentId();
        CommentCreateDto replyDto1 = new CommentCreateDto(savedPostId1, memberId, "대댓글1", savedCommentId1);
        CommentCreateDto replyDto2 = new CommentCreateDto(savedPostId1, memberId, "대댓글2", savedCommentId2);
        Long savedReplyId1 = commentService.createComment(replyDto1).getCommentId();
        Long savedReplyId2 = commentService.createComment(replyDto2).getCommentId();

        em.flush();
        em.clear();
        //when
        postService.deletePost(savedPostId1, memberId);

        //then
        assertThatThrownBy(() -> postService.getPostWithDetail(savedPostId1, memberId))
                .isInstanceOf(NotFoundException.class);

        assertThat(commentRepository.findById(savedCommentId1).isEmpty()).isTrue();
        assertThat(commentRepository.findById(savedCommentId2).isEmpty()).isTrue();
        assertThat(commentRepository.findById(savedReplyId1).isEmpty()).isTrue();
        assertThat(commentRepository.findById(savedReplyId2).isEmpty()).isTrue();
    }

    @Test
    void postDeleteWithLikesTest() {
        //given
        MemberDto memberDto1 = new MemberDto("member1", "member11", "", "");
        MemberDto memberDto2 = new MemberDto("member2", "member22", "", "");
        Long memberId1 = memberService.createUser(memberDto1);
        Long memberId2 = memberService.createUser(memberDto2);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        PostAddRequest postAddRequest1 = new PostAddRequest(memberId1, "게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(postAddRequest1).getPostId();

        //좋아요
        likedService.changeMemberPostLiked(savedPostId1, memberId1);
        likedService.changeMemberPostLiked(savedPostId1, memberId2);

        em.flush();
        em.clear();

        //when
        postService.deletePost(savedPostId1, memberId1);

        //then
        assertThat(memberPostLikedRepository.findByMemberIdAndPostId(memberId1, savedPostId1).isEmpty()).isTrue();
        assertThat(memberPostLikedRepository.findByMemberIdAndPostId(memberId2, savedPostId1).isEmpty()).isTrue();
    }

    @Test
    void postDeleteWithLikesAndCommentsTest() {
        //given
        MemberDto memberDto1 = new MemberDto("member1", "member11", "", "");
        MemberDto memberDto2 = new MemberDto("member2", "member22", "", "");
        Long memberId1 = memberService.createUser(memberDto1);
        Long memberId2 = memberService.createUser(memberDto2);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        PostAddRequest postAddRequest1 = new PostAddRequest(memberId1, "게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(postAddRequest1).getPostId();

        //좋아요
        likedService.changeMemberPostLiked(savedPostId1, memberId1);
        likedService.changeMemberPostLiked(savedPostId1, memberId2);

        //댓글 달기
        CommentCreateDto commentDto1 = new CommentCreateDto(savedPostId1, memberId1, "댓글1", null);
        CommentCreateDto commentDto2 = new CommentCreateDto(savedPostId1, memberId2, "댓글2", null);
        Long savedCommentId1 = commentService.createComment(commentDto1).getCommentId();
        Long savedCommentId2 = commentService.createComment(commentDto2).getCommentId();
        CommentCreateDto replyDto1 = new CommentCreateDto(savedPostId1, memberId2, "대댓글1", savedCommentId1);
        CommentCreateDto replyDto2 = new CommentCreateDto(savedPostId1, memberId1, "대댓글2", savedCommentId2);
        Long savedReplyId1 = commentService.createComment(replyDto1).getCommentId();
        Long savedReplyId2 = commentService.createComment(replyDto2).getCommentId();

        em.flush();
        em.clear();

        //when
        postService.deletePost(savedPostId1, memberId1);

        //then
        assertThat(memberPostLikedRepository.findByMemberIdAndPostId(memberId1, savedPostId1).isEmpty()).isTrue();
        assertThat(memberPostLikedRepository.findByMemberIdAndPostId(memberId2, savedPostId1).isEmpty()).isTrue();

        assertThat(commentRepository.findById(savedCommentId1).isEmpty()).isTrue();
        assertThat(commentRepository.findById(savedCommentId2).isEmpty()).isTrue();
        assertThat(commentRepository.findById(savedReplyId1).isEmpty()).isTrue();
        assertThat(commentRepository.findById(savedReplyId2).isEmpty()).isTrue();
    }
}