package heavysnow.heath.service;

import heavysnow.heath.domain.Comment;
import heavysnow.heath.domain.Post;
import heavysnow.heath.dto.postdto.PostDetailResponseDto;
import heavysnow.heath.dto.postdto.PostListResponseDto;
import heavysnow.heath.repository.CommentRepository;
import heavysnow.heath.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    /**
     * 마이페이지에 사용되는 게시글 리스트를 가져오는 메서드
     * @param memberId: 회원 아이디
     * @param page: 페이지 넘버
     * @return PostListResponseDto
     */
    public PostListResponseDto getPostListByMember(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page, 9);
        Slice<Post> postSlice = postRepository.findPageByMember(memberId, pageable);
        return PostListResponseDto.of(postSlice);
    }

    /**
     * 메인페이지에 사용되는 게시글 리스트를 가져오는 메서드
     * @param page: 페이지 넘버
     * @param sort: 정렬할 컬럼
     * @return PostListResponseDto
     */
    public PostListResponseDto getPostList(int page, String sort) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, sort));
        Slice<Post> postSlice = postRepository.findPage(pageable);
        return PostListResponseDto.of(postSlice);
    }

    /**
     * 게시글 상세 페이지에 사용되는 게시글 상세 정보를 가져오는 메서드
     * @param postId: postId가 일치하는 하나의 게시글을 가져옴
     * @param memberId: 이때 현재 접속한 회원이 이 게시글에 좋아요를 눌렀는지 확인할 수 있어야함
     * @return PostDetailResponseDto
     */
    public PostDetailResponseDto getPostWithDetail(Long postId, Long memberId) {
        Post findPost = postRepository.findPostDetailById(postId).orElseThrow(); //커스텀 예외 추가하여 catch 해야함
        List<Comment> findComments = commentRepository.findWithMemberByPostId(postId);
//        boolean isLiked = isMemberPostLiked(postId, memberId);
        return PostDetailResponseDto.of(findPost, memberId, findComments);
    }


    /**
     * 미완성 - MemberPostLikedRepository가 생성된 후에 추가 구현 필요
     * findPost의 MemberPostLikedList에서 memberId가 있는지를 비교하는 것과 쿼리 한번을 더 하는 것의 성능 차이 확인 필요
     *
     * memberId를 가진 회원이 postId인 게시글에 좋아요를 눌렀는지를 반환
     * @param postId
     * @param memberId
     * @return
     */
    private boolean isMemberPostLiked(Long postId, Long memberId) {
        //MemberPostLikedRepository가 생성된 후에 다시 구현
        return true;
    }
}
