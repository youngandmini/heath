package heavysnow.heath.service;

import heavysnow.heath.dto.comment.CommentCreateRequest;
import heavysnow.heath.dto.member.MemberRequest;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.dto.post.PostEditRequest;
import heavysnow.heath.dto.post.PostDetailResponse;
import heavysnow.heath.dto.post.PostListResponse;
import heavysnow.heath.exception.NotFoundException;
import heavysnow.heath.repository.CommentRepository;
import heavysnow.heath.repository.MemberPostLikedRepository;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    PostRepository postRepository;
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

    @BeforeEach
    void before() {
        //mysql의 date_format 형태의 sql 문법을 h2 DB에서 구동될 수 있도록 ALIAS를 등록
        em.createNativeQuery("CREATE ALIAS IF NOT EXISTS date_format FOR \"heavysnow.heath.alias.H2DateFormatAlias.date_format\"")
                .executeUpdate();
    }

    @Test
    void getPostListByMember() {
        //given
        MemberRequest memberRequest = new MemberRequest("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberRequest);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        List<String> imgUrls2 = new ArrayList<>();
        imgUrls2.add("이미지21");
        imgUrls2.add("이미지22");
        imgUrls2.add("이미지23");

        PostAddRequest postAddRequest1 = new PostAddRequest("게시글 제목1", "게시글 내용1", imgUrls1);
        PostAddRequest postAddRequest2 = new PostAddRequest("게시글 제목2", "게시글 내용2", imgUrls2);
        Long savedPostId1 = postService.writePost(memberId, postAddRequest1).getPostId();
        Long savedPostId2 = postService.writePost(memberId, postAddRequest2).getPostId();

        //when - 멤버별로 최신순으로 검색한다.
        PostListResponse responseDto = postService.getPostListByMember(memberId, 1);

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
        MemberRequest memberRequest1 = new MemberRequest("member1", "member11", "", "");
        MemberRequest memberRequest2 = new MemberRequest("member2", "member22", "", "");
        MemberRequest memberRequest3 = new MemberRequest("member3", "member33", "", "");
        Long memberId1 = memberService.createUser(memberRequest1);
        Long memberId2 = memberService.createUser(memberRequest2);
        Long memberId3 = memberService.createUser(memberRequest3);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        List<String> imgUrls2 = new ArrayList<>();
        imgUrls2.add("이미지2");
        List<String> imgUrls3 = new ArrayList<>();
        imgUrls3.add("이미지3");
        List<String> imgUrls4 = new ArrayList<>();
        imgUrls4.add("이미지4");

        PostAddRequest postAddRequest1 = new PostAddRequest("게시글 제목1", "게시글 내용1", imgUrls1);
        PostAddRequest postAddRequest2 = new PostAddRequest("게시글 제목2", "게시글 내용2", imgUrls2);
        PostAddRequest postAddRequest3 = new PostAddRequest("게시글 제목3", "게시글 내용3", imgUrls3);
        PostAddRequest postAddRequest4 = new PostAddRequest("게시글 제목4", "게시글 내용4", imgUrls4);

        Long savedPostId1 = postService.writePost(memberId1, postAddRequest1).getPostId();
        Long savedPostId2 = postService.writePost(memberId2, postAddRequest2).getPostId();
        Long savedPostId3 = postService.writePost(memberId3, postAddRequest3).getPostId();
        Long savedPostId4 = postService.writePost(memberId3, postAddRequest4).getPostId();

        // 좋아요를 누름
        likedService.changeMemberPostLiked(savedPostId1, memberId1);
        likedService.changeMemberPostLiked(savedPostId1, memberId2);
        likedService.changeMemberPostLiked(savedPostId1, memberId3);

        likedService.changeMemberPostLiked(savedPostId2, memberId1);
        likedService.changeMemberPostLiked(savedPostId2, memberId2);

        likedService.changeMemberPostLiked(savedPostId4, memberId1);

        //when
        PostListResponse responseDto1 = postService.getPostList(1, "liked");

        //then
        assertThat(responseDto1.getPosts().get(0).getPostId()).isEqualTo(savedPostId1);
        assertThat(responseDto1.getPosts().get(1).getPostId()).isEqualTo(savedPostId2);
        assertThat(responseDto1.getPosts().get(2).getPostId()).isEqualTo(savedPostId4);
        assertThat(responseDto1.getPageInfo().getNumberOfElements()).isEqualTo(3);

        //when
        PostListResponse responseDto2 = postService.getPostList(1, "createdDate");

        //then
        assertThat(responseDto2.getPageInfo().getNumberOfElements()).isEqualTo(3);
        assertThat(responseDto2.getPosts().get(0).getPostId()).isEqualTo(savedPostId4);
        assertThat(responseDto2.getPosts().get(1).getPostId()).isEqualTo(savedPostId3);
        assertThat(responseDto2.getPosts().get(2).getPostId()).isEqualTo(savedPostId2);

    }

    @Test
    void getPostWithDetail() {
        //given
        MemberRequest memberRequest = new MemberRequest("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberRequest);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        List<String> imgUrls2 = new ArrayList<>();
        imgUrls2.add("이미지21");
        imgUrls2.add("이미지22");
        imgUrls2.add("이미지23");

        PostAddRequest postAddRequest1 = new PostAddRequest("게시글 제목1", "게시글 내용1", imgUrls1);
        PostAddRequest postAddRequest2 = new PostAddRequest("게시글 제목2", "게시글 내용2", imgUrls2);
        Long savedPostId1 = postService.writePost(memberId, postAddRequest1).getPostId();
        Long savedPostId2 = postService.writePost(memberId, postAddRequest2).getPostId();

        //when
        PostDetailResponse responseDto = postService.getPostWithDetail(savedPostId1, memberId);

        //then
        assertThat(responseDto.getPostId()).isEqualTo(savedPostId1);
        assertThat(responseDto.getMemberId()).isEqualTo(memberId);
        assertThat(responseDto.getTitle()).isEqualTo("게시글 제목1");
        assertThat(responseDto.getContent()).isEqualTo("게시글 내용1");
        assertThat(responseDto.getLiked()).isEqualTo(0);
        assertThat(responseDto.getConsecutiveDays()).isEqualTo(1);
        assertThat(responseDto.getPostImgUrls().size()).isEqualTo(3);
        assertThat(responseDto.getPostImgUrls().get(0)).isEqualTo("이미지1");
        assertThat(responseDto.isLiked()).isFalse();

    }


    @Test
    void postUpdateTest() {
        //given
        MemberRequest memberRequest = new MemberRequest("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberRequest);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        PostAddRequest postAddRequest1 = new PostAddRequest("게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(memberId, postAddRequest1).getPostId();

        //when
        List<String> editImgUrls = new ArrayList<>();
        editImgUrls.add("수정 이미지1");
        editImgUrls.add("수정 이미지2");
        editImgUrls.add("수정 이미지3");
        editImgUrls.add("수정 이미지4");
        PostEditRequest editRequest = new PostEditRequest("수정 제목1", "수정 게시글 내용1", editImgUrls);
        postService.editPost(savedPostId1, editRequest, memberId);
        PostDetailResponse responseDto = postService.getPostWithDetail(savedPostId1, memberId);

        //then
        assertThat(responseDto.getPostId()).isEqualTo(savedPostId1);
        assertThat(responseDto.getMemberId()).isEqualTo(memberId);
        assertThat(responseDto.getTitle()).isEqualTo("수정 제목1");
        assertThat(responseDto.getContent()).isEqualTo("수정 게시글 내용1");
        assertThat(responseDto.getLiked()).isEqualTo(0);
        assertThat(responseDto.getConsecutiveDays()).isEqualTo(1);
        assertThat(responseDto.getPostImgUrls().size()).isEqualTo(4);
        assertThat(responseDto.getPostImgUrls().get(0)).isEqualTo("수정 이미지1");
        assertThat(responseDto.getPostImgUrls().get(1)).isEqualTo("수정 이미지2");
        assertThat(responseDto.getPostImgUrls().get(2)).isEqualTo("수정 이미지3");
        assertThat(responseDto.getPostImgUrls().get(3)).isEqualTo("수정 이미지4");

    }

    @Test
    void postDeleteTest() {
        //given
        MemberRequest memberRequest = new MemberRequest("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberRequest);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        PostAddRequest postAddRequest1 = new PostAddRequest("게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(memberId, postAddRequest1).getPostId();

        //when
        postService.deletePost(savedPostId1, memberId);

        //then
        assertThatThrownBy(() -> postService.getPostWithDetail(savedPostId1, memberId))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    void postDeleteWithCommentsTest() {
        //given
        MemberRequest memberRequest = new MemberRequest("member1", "member11", "", "");
        Long memberId = memberService.createUser(memberRequest);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");

        PostAddRequest postAddRequest1 = new PostAddRequest("게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(memberId, postAddRequest1).getPostId();

        //댓글 달기
        CommentCreateRequest commentDto1 = new CommentCreateRequest("댓글1");
        CommentCreateRequest commentDto2 = new CommentCreateRequest("댓글2");

        Long savedCommentId1 = commentService.createComment(savedPostId1, commentDto1, null, memberId).getCommentId();
        Long savedCommentId2 = commentService.createComment(savedPostId1, commentDto2, null, memberId).getCommentId();

        CommentCreateRequest replyDto1 = new CommentCreateRequest("대댓글1");
        CommentCreateRequest replyDto2 = new CommentCreateRequest("대댓글2");
        Long savedReplyId1 = commentService.createComment(savedPostId1, replyDto1, savedCommentId1, memberId).getCommentId();
        Long savedReplyId2 = commentService.createComment(savedPostId1, replyDto2, savedCommentId2, memberId).getCommentId();

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
        MemberRequest memberRequest1 = new MemberRequest("member1", "member11", "", "");
        MemberRequest memberRequest2 = new MemberRequest("member2", "member22", "", "");
        Long memberId1 = memberService.createUser(memberRequest1);
        Long memberId2 = memberService.createUser(memberRequest2);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        PostAddRequest postAddRequest1 = new PostAddRequest("게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(memberId1, postAddRequest1).getPostId();

        //좋아요
        likedService.changeMemberPostLiked(savedPostId1, memberId1);
        likedService.changeMemberPostLiked(savedPostId1, memberId2);

        em.flush();
        em.clear();

        //when
        postService.deletePost(savedPostId1, memberId1);

        em.flush();
        em.clear();

        //then
        assertThat(memberPostLikedRepository.findByMemberIdAndPostId(memberId1, savedPostId1).isEmpty()).isTrue();
        assertThat(memberPostLikedRepository.findByMemberIdAndPostId(memberId2, savedPostId1).isEmpty()).isTrue();
    }

    @Test
    void postDeleteWithLikesAndCommentsTest() {
        //given
        MemberRequest memberRequest1 = new MemberRequest("member1", "member11", "", "");
        MemberRequest memberRequest2 = new MemberRequest("member2", "member22", "", "");
        Long memberId1 = memberService.createUser(memberRequest1);
        Long memberId2 = memberService.createUser(memberRequest2);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        PostAddRequest postAddRequest1 = new PostAddRequest("게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(memberId1, postAddRequest1).getPostId();

        //좋아요
        likedService.changeMemberPostLiked(savedPostId1, memberId1);
        likedService.changeMemberPostLiked(savedPostId1, memberId2);

        //댓글 달기
        CommentCreateRequest commentDto1 = new CommentCreateRequest("댓글1");
        CommentCreateRequest commentDto2 = new CommentCreateRequest("댓글2");

        Long savedCommentId1 = commentService.createComment(savedPostId1, commentDto1, null, memberId1).getCommentId();
        Long savedCommentId2 = commentService.createComment(savedPostId1, commentDto2, null, memberId2).getCommentId();

        CommentCreateRequest replyDto1 = new CommentCreateRequest("대댓글1");
        CommentCreateRequest replyDto2 = new CommentCreateRequest("대댓글2");
        Long savedReplyId1 = commentService.createComment(savedPostId1, replyDto1, savedCommentId1, memberId2).getCommentId();
        Long savedReplyId2 = commentService.createComment(savedPostId1, replyDto2, savedCommentId2, memberId1).getCommentId();

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

    @Test
    @DisplayName("회원을 삭제하면 post도 함께 문제 없이 삭제되어야한다.")
    void deleteMemberWithPosts() {
        MemberRequest memberRequest = MemberRequest.builder()
                .username("aaaa@example.com")
                .nickname("gwan")
                .userStatusMessage("")
                .profileImgUrl("imgUrl")
                .build();
        Long savedMemberId = memberService.createUser(memberRequest);

        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        PostAddRequest postAddRequest1 = new PostAddRequest("게시글 제목1", "게시글 내용1", imgUrls1);
        Long savedPostId1 = postService.writePost(savedMemberId, postAddRequest1).getPostId();


        em.flush();
        em.clear();

        memberService.deleteMember(savedMemberId, savedMemberId);

        assertThatThrownBy(() -> memberService.findMemberWithGoals(savedMemberId))
                .isInstanceOf(NotFoundException.class);

        assertThat(postRepository.findById(savedPostId1).isEmpty()).isTrue();

    }
}