package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.domain.Post;
import heavysnow.heath.domain.PostImage;
import heavysnow.heath.domain.Comment;
import heavysnow.heath.dto.PostDatesResponseDto;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.dto.post.PostDeleteRequest;
import heavysnow.heath.dto.post.PostEditRequest;
import heavysnow.heath.dto.postdto.PostDetailResponseDto;
import heavysnow.heath.dto.postdto.PostListResponseDto;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.repository.PostImageRepository;
import heavysnow.heath.repository.PostRepository;
import heavysnow.heath.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;

    // 게시글 등록
    // postId를 반환하도록 변경
    @Transactional
    public Long writePost(PostAddRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 멤버를 찾을 수 없습니다."));

        // consecutiveDays 계산
        int consecutiveDays;
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Optional<Post> postOptional = postRepository.findByCreatedDate(yesterday);
        consecutiveDays = postOptional.map(post -> post.getConsecutiveDays() + 1).orElse(1);

        // post 생성
        Post post = new Post(member, request.getTitle(), request.getContent(), consecutiveDays);
        postRepository.save(post);

        // image 및 mainImage 생성
        String mainImg = request.getImages().get(0);
        PostImage postMainImage = new PostImage(post, mainImg);
        postImageRepository.save(postMainImage);
        post.setMainImage(postMainImage);

        for (int i = 1; i < request.getImages().size(); i++) {
            String imgUrl = request.getImages().get(i);
            PostImage postImage = new PostImage(post, imgUrl);
            postImageRepository.save(postImage);
        }

        return post.getId();
    }

    // 게시글 수정
    @Transactional
    public void editPost(PostEditRequest request) {
        Optional<Post> postOptional = postRepository.findById(request.getPostId());
        Post post = postOptional.orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        // title, content 수정
        post.update(request.getTitle(), request.getContent());

        List<PostImage> oldImages = post.getPostImages();
        List<String> editImages = request.getPostImages();

        // image 추가 및 삭제
        imageUpdate(post, oldImages, editImages);

        //image 전부 삭제 후 다시 추가
//        allDeleteAndAddImage(request, post);

        postRepository.save(post);
    }

    // 기존 이미지와 비교하여 image 추가 및 삭제
    private void imageUpdate(Post post, List<PostImage> oldImages, List<String> editImages) {
        // image 삭제
        post.setMainImage(null);
        List<PostImage> deleteImages = oldImages.stream().filter(
                o -> !editImages.contains(o.getImgUrl())
        ).collect(Collectors.toList());

        postImageRepository.deletePostImages(deleteImages);

        for (PostImage postImage : deleteImages) {
            post.getPostImages().remove(postImage);
        }

        // image 추가
        List<String> oldImgUrls = oldImages.stream()
                .map(PostImage::getImgUrl)
                .collect(Collectors.toList());

        List<String> addImages = editImages.stream().filter(
                e -> !oldImgUrls.contains(e)
        ).collect(Collectors.toList());

        for (String imgUrl : addImages) {
            PostImage postImage = new PostImage(post, imgUrl);
            postImageRepository.save(postImage);
        }

        PostImage mainImage = postImageRepository.findByUrl(editImages.get(0));
        post.setMainImage(mainImage);
    }

    // image 전부 삭제 후 다시 추가
    private void allDeleteAndAddImage(PostEditRequest request, Post post) {
        post.setMainImage(null);
        postImageRepository.deletePostImagesByPostId(post.getId());
        post.getPostImages().clear();

        String mainImg = request.getPostImages().get(0);
        PostImage postMainImage = new PostImage(post, mainImg);
        postImageRepository.save(postMainImage);
        post.setMainImage(postMainImage);

        for (int i = 1; i < request.getPostImages().size(); i++) {
            String imgUrl = request.getPostImages().get(i);
            PostImage postImage = new PostImage(post, imgUrl);
            postImageRepository.save(postImage);
        }
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Post post = postOptional.orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        /**
         * 댓글 삭제 추후 수정 필요
         */

        // postImages도 같이 삭제 됨
        postRepository.delete(post);
    }
  
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
     * 마이페이지에 사용되는 해당월 운동 일수를 가져오는 메서드
     * memberId를 갖는 회원이 year년 month월에 쓴 게시글의 날짜들을 반환
     * @param memberId
     * @param year
     * @param month
     * @return
     */
    public PostDatesResponseDto getPostDates(Long memberId, int year, int month) {
        List<LocalDateTime> postDatetimes = postRepository.findDatesByMemberAndYearMonth(memberId, year, month);
        return PostDatesResponseDto.of(postDatetimes);
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
