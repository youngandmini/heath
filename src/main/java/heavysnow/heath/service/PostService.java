package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.domain.Post;
import heavysnow.heath.domain.PostImage;
import heavysnow.heath.dto.post.PostAddRequest;
import heavysnow.heath.dto.post.PostDeleteRequest;
import heavysnow.heath.dto.post.PostEditRequest;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.repository.PostImageRepository;
import heavysnow.heath.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    // 게시글 등록
    @Transactional
    public Post writePost(PostAddRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 멤버를 찾을 수 없습니다."));

        // consecutiveDays 계산
        int consecutiveDays;

        LocalDate yesterday = LocalDate.now().minusDays(1);
        Optional<Post> postOptional = postRepository.findByCreatedDate(yesterday);
        if (postOptional.isPresent()) {
            consecutiveDays = postOptional.get().getConsecutiveDays() + 1;
        }
        else {
            consecutiveDays = 1;
        }

        // post 생성
        Post post = new Post(member, request.getTitle(), request.getContent(), consecutiveDays, 0);
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

        return post;
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

        // mainImage 설정
        if (post.getMainImage().getImgUrl() != editImages.get(0)) {
            PostImage mainImage = postImageRepository.findByUrl(editImages.get(0));
            post.setMainImage(mainImage);
        }

        // allDeleteAndAddImage(request, post);

        postRepository.save(post);
    }

    // 기존 이미지와 비교하여 image 추가 및 삭제
    private void imageUpdate(Post post, List<PostImage> oldImages, List<String> editImages) {
        // image 삭제
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
    }

    // image 전부 삭제 후 다시 추가
    private void allDeleteAndAddImage(PostEditRequest request, Post post) {
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
    public void deletePost(PostDeleteRequest request) {
        Optional<Post> postOptional = postRepository.findById(request.getPostId());
        Post post = postOptional.orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        /**
         * 댓글 삭제 추후 수정 필요
         */

        // postImages도 같이 삭제 됨
        postRepository.delete(post);
    }



}
