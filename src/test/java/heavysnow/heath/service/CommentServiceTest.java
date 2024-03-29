package heavysnow.heath.service;

import heavysnow.heath.domain.Comment;
import heavysnow.heath.domain.Member;
import heavysnow.heath.dto.comment.CommentCreateRequest;
import heavysnow.heath.dto.comment.CommentUpdateRequest;
import heavysnow.heath.dto.member.MemberRequest;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.repository.CommentRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CommentServiceTest {


    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EntityManager em;

    private Long savedMemberId1;

    private Long savedMemberId2;
    private Long savedPostId1;
    private Long savedPostId2;


    @BeforeEach
    void before() {
        //mysql의 date_format 형태의 sql 문법을 h2 DB에서 구동될 수 있도록 ALIAS를 등록
        em.createNativeQuery("CREATE ALIAS IF NOT EXISTS date_format FOR \"heavysnow.heath.alias.H2DateFormatAlias.date_format\"")
                .executeUpdate();

        /**
         * 멤버 생성
         * 멤버1(savedMemberId1)과 멤버2(savedMemberId2)를 생성한다.
         * 포스트는 멤버1(savedMemberId1)의 포스트 이다.
         * 멤버1(savedMemberId1)의 포스트의 개수는 총2개 이다.
         */
        MemberRequest memberRequest = MemberRequest.builder()
                .username("leejungbin")
                .nickname("ego2")
                .userStatusMessage("fighting!!!")
                .profileImgUrl("None")
                .build();

        // memberDto에 있는 정보 데베에 저장
        savedMemberId1 = memberService.createUser(memberRequest);

        Member member = memberRepository.findById(savedMemberId1).get();

        memberRequest = MemberRequest.builder()
                .username("leejungbin111")
                .nickname("ego222222")
                .userStatusMessage("fighting!!!")
                .profileImgUrl("None")
                .build();

        // memberDto에 있는 정보 데베에 저장
        savedMemberId2 = memberService.createUser(memberRequest);


        /**
         * post 생성
         */
        List<String> imgUrls1 = new ArrayList<>();
        imgUrls1.add("이미지1");
        imgUrls1.add("이미지2");
        imgUrls1.add("이미지3");

        PostAddRequest postAddRequest1 = new PostAddRequest("게시글 제목1", "게시글 내용1", imgUrls1);
        PostAddRequest postAddRequest2 = new PostAddRequest("게시글 제목2", "게시글 내용2", imgUrls1);
        savedPostId1 = postService.writePost(member.getId(), postAddRequest1).getPostId();
        savedPostId2 = postService.writePost(member.getId(), postAddRequest2).getPostId();
    }


    @DisplayName("createComment : 포스트에 댓글 생성 확인")
    @Test
    public void createComment() throws Exception {
        // given
        String commentContent1 = "제 게시글 입니다.";
        String commentContent2 = "잘 보고 갑니다~";
        // 자기 자신 댓글
        CommentCreateRequest commentCreateRequest1 = new CommentCreateRequest(commentContent1);
        // 타인 댓글
        CommentCreateRequest commentCreateRequest2 = new CommentCreateRequest(commentContent2);


        // when
        Long savedcommentId1 = commentService.createComment(savedPostId1, commentCreateRequest1, null, savedMemberId1).getCommentId();
        Long savedcommentId2 = commentService.createComment(savedPostId1, commentCreateRequest2, null, savedMemberId2).getCommentId();

        // then
        Comment foundComment1 = commentRepository.findById(savedcommentId1)
                .orElseThrow(() -> new AssertionError("댓글이 데이터 베이스에 없다."));

        Comment foundComment2 = commentRepository.findById(savedcommentId2)
                .orElseThrow(() -> new AssertionError("댓글이 데이터 베이스에 없다."));


        assertThat(foundComment1.getContent()).isEqualTo(commentContent1);
        assertThat(foundComment1.getMember().getId()).isEqualTo(savedMemberId1);
        assertThat(foundComment1.getPost().getId()).isEqualTo(savedPostId1);


        assertThat(foundComment2.getContent()).isEqualTo(commentContent2);
        assertThat(foundComment2.getMember().getId()).isEqualTo(savedMemberId2);
        assertThat(foundComment2.getPost().getId()).isEqualTo(savedPostId1);
    }


    @DisplayName("createReplyComment : 대댓글 생성 확인")
    @Test
    public void createReplyComment() throws Exception {
        // given
        String commentContent1 = "제 게시글 입니다.";
        String replyComment = "대댓글 입니다.";

        //댓글 저장
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest(commentContent1);
        Long parentCommentId = commentService.createComment(savedPostId1, commentCreateRequest, null, savedMemberId1).getCommentId();

        CommentCreateRequest childCommentDto = new CommentCreateRequest(replyComment);

        // when
        Long replyCommentId = commentService.createComment(savedPostId1, childCommentDto, parentCommentId, savedMemberId1).getCommentId();

        // then
        Comment foundReplyComment = commentRepository.findById(replyCommentId)
                .orElseThrow(() -> new AssertionError("대댓글이 데이터 베이스에 없다."));

        assertThat(foundReplyComment.getContent()).isEqualTo(replyComment);
        assertThat(foundReplyComment.getMember().getId()).isEqualTo(savedMemberId1);

        assertThat(foundReplyComment.getParentComment()).isNotNull();
        // 대댓글의 부모 아이디가 일치 해야한다.
        assertThat(foundReplyComment.getParentComment().getId()).isEqualTo(parentCommentId);
    }

    @DisplayName("updateComment : 댓글이 수정되어야 한다.")
    @Test
    public void updateComment() throws Exception {
        // given
        String updateContent = "수정된 댓글 입니다.";

        CommentCreateRequest commentCreateRequest = new CommentCreateRequest("원래 댓글입니다.");
        Long savedCommentId = commentService.createComment(savedPostId1, commentCreateRequest, null, savedMemberId1).getCommentId();

        // when
        CommentUpdateRequest commentUpdateRequest = new CommentUpdateRequest(updateContent);
        commentService.updateComment(savedPostId1, savedCommentId, commentUpdateRequest, savedMemberId1);

        Comment updatedComment = commentRepository.findById(savedCommentId).get();

        // then
        assertThat(updatedComment.getContent()).isEqualTo(updateContent);
    }

    @DisplayName("deleteComment: 댓글 삭제 기능.")
    @Test
    void deleteCommentTest(){
        //given
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest("안녕하세요.");
        Long savedCommentId = commentService.createComment(savedPostId1, commentCreateRequest, null, savedMemberId1).getCommentId();

        // when
        commentService.deleteComment(savedCommentId, savedMemberId1);
        Comment comment = commentRepository.findById(savedCommentId).orElse(null);

        // then
        assertThat(comment).isNull();
    }

    @DisplayName("댓글이 삭제 되었을때, 답글이 삭제되었는지 확인.")
    @Test
    void deleteReplyTest() {
        //given
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest("안녕하세요.");
        Long savedCommentId = commentService.createComment(savedPostId1, commentCreateRequest, null, savedMemberId1).getCommentId();
        CommentCreateRequest replyCreateDto = new CommentCreateRequest("안녕하세요.");
        Long savedReplyId = commentService.createComment(savedPostId1, replyCreateDto, savedCommentId, savedMemberId1).getCommentId();

        // when
        commentService.deleteComment(savedCommentId, savedMemberId1);
//        Comment comment = commentRepository.findById(savedReplyId).orElse(null);
        em.flush();
        em.clear();

        // then
        assertThat(commentRepository.findById(savedReplyId).isEmpty()).isTrue();
//        assertThat(comment).isNull();
    }

}

