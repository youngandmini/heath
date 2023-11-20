package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.domain.Post;
import heavysnow.heath.domain.PostImage;
import heavysnow.heath.domain.Comment;
import heavysnow.heath.dto.post.PostDatesResponse;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.dto.post.PostAddResponse;
import heavysnow.heath.dto.post.PostEditRequest;
import heavysnow.heath.dto.post.PostDetailResponse;
import heavysnow.heath.dto.post.PostListResponse;
import heavysnow.heath.exception.BadRequestException;
import heavysnow.heath.exception.ForbiddenException;
import heavysnow.heath.exception.NotFoundException;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.repository.PostImageRepository;
import heavysnow.heath.repository.PostRepository;
import heavysnow.heath.repository.CommentRepository;
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


    /**
     * 게시글을 등록하는 메서드
     * @param loginMemberId: 해당 멤버로 접속하여
     * @param request: 해당 정보로 게시글을 등록
     * @return: 게시글의 id를 반환
     */
    @Transactional
    public PostAddResponse writePost(Long loginMemberId, PostAddRequest request) {
        Member member = memberRepository.findById(loginMemberId).orElseThrow();

        // consecutiveDays 계산
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Optional<Post> postOptional = postRepository.findByCreatedDate(member.getId(), yesterday);
        int consecutiveDays = postOptional.map(post -> post.getConsecutiveDays() + 1).orElse(1);

        // post 생성
        Post post = new Post(member, request.getTitle(), request.getContent(), consecutiveDays);
        postRepository.save(post);

        // image 및 mainImage 생성
        String mainImg = request.getPostImgUrls().get(0);
        PostImage postMainImage = new PostImage(post, mainImg);
        postImageRepository.save(postMainImage);
        post.setMainImage(postMainImage);

        for (int i = 1; i < request.getPostImgUrls().size(); i++) {
            String imgUrl = request.getPostImgUrls().get(i);
            PostImage postImage = new PostImage(post, imgUrl);
            postImageRepository.save(postImage);
        }

        return PostAddResponse.of(post);
    }


    /**
     * 게시글을 수정하는 메서드
     * @param postId: 해당 게시글을
     * @param request: 해당 정보를 이용해
     * @param loginMemberId: 해당 접속한 멤버가 게시글을 수정 요청
     */
    @Transactional
    public void editPost(Long postId, PostEditRequest request, Long loginMemberId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundException::new);

        if (!post.getMember().getId().equals(loginMemberId)) {
            throw new ForbiddenException();
        }

        // title, content 수정
        post.update(request.getTitle(), request.getContent());

        // 이미지 수정
        List<PostImage> oldImages = post.getPostImages();
        List<String> editImages = request.getPostImgUrls();
        imageUpdate(post, oldImages, editImages);

        postRepository.save(post);
    }

    /**
     * 기존에 저장된 이미지와 새로 요청된 이미지를 비교하여, 추가 및 삭제
     * @param post: 해당 게시글에
     * @param oldImages: 기존의 이미지와
     * @param editImages: 새로 요청된 이미지를 비교하여 추가 / 삭제를 수행
     */
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

        Optional<PostImage> mainImageOptional = postImageRepository.findByUrl(editImages.get(0));
        PostImage mainImage = mainImageOptional.orElseThrow();
        post.setMainImage(mainImage);
    }

    /**
     * 게시글을 삭제하는 메서드
     * @param postId: 해당 게시글을
     * @param loginMemberId: 해당 접속한 멤버가 삭제 요청
     */
    @Transactional
    public void deletePost(Long postId, Long loginMemberId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Post post = postOptional.orElseThrow(NotFoundException::new);

        if (!post.getMember().getId().equals(loginMemberId)) {
            throw new ForbiddenException();
        }

        postRepository.delete(post);
    }
  
    /**
     * 마이페이지에 사용되는 게시글 리스트를 가져오는 메서드
     * @param memberId: 해당 회원의
     * @param page: 게시글의 해당 페이지를 요청
     * @return 게시글 리스트를 반환
     */
    public PostListResponse getPostListByMember(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page-1, 9);
        Slice<Post> postSlice = postRepository.findPageByMember(memberId, pageable);
        return PostListResponse.of(postSlice);
    }

    /**
     * 메인페이지에 사용되는 게시글 리스트를 가져오는 메서드
     * @param page: 게시글의 해당 페이지를
     * @param sort: 해당 컬럼으로 정렬하여 조회하도록 요청
     * @return 게시글 리스트를 반환
     */
    public PostListResponse getPostList(int page, String sort) {
//        Pageable pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, sort));
//        Slice<Post> postSlice = postRepository.findPage(pageable);
//        return PostListResponse.of(postSlice);

        Pageable pageable = PageRequest.of(page-1, 3);
        if (sort.equals("createdDate")) {
            Slice<Post> pageOrderByCreatedDate = postRepository.findPageOrderByCreatedDate(pageable);
            return PostListResponse.of(pageOrderByCreatedDate);
        } else if (sort.equals("liked")) {
            Slice<Post> pageOrderByLiked = postRepository.findPageOrderByLiked(pageable);
            return PostListResponse.of(pageOrderByLiked);
        }

        throw new BadRequestException();
    }

    /**
     * 게시글 상세 페이지에 사용되는 게시글 상세 정보를 가져오는 메서드
     * @param postId: 해당 게시글에 대한 상세 정보를
     * @param loginMemberId: 해당 접속한 멤버가 요청 (좋아요 여부를 알기 위해)
     * @return 게시글 상세정보를 반환
     */
    public PostDetailResponse getPostWithDetail(Long postId, Long loginMemberId) {
        Post findPost = postRepository.findPostDetailById(postId).orElseThrow(NotFoundException::new);
        List<Comment> findComments = commentRepository.findWithMemberByPostId(postId);
        return PostDetailResponse.of(findPost, loginMemberId, findComments);
    }


    /**
     * 마이페이지에 사용되는 해당월 운동 일수를 가져오는 메서드
     * @param memberId: 해당 멤버의
     * @param year: 해당 년
     * @param month: 해당 월에 쓴  게시글의 날짜들을 반환
     * @return: 날짜 리스트를 반환
     */
    public PostDatesResponse getPostDates(Long memberId, int year, int month) {
        List<LocalDateTime> postDatetimes = postRepository.findDatesByMemberAndYearMonth(memberId, year, month);
        return PostDatesResponse.of(postDatetimes);
    }
  
}
