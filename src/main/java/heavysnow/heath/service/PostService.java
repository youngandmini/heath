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

import java.awt.*;
import java.util.ArrayList;
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
    public Long writePost(PostAddRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 멤버를 찾을 수 없습니다."));

        // title, content 생성
        Post post = new Post(member, request.getTitle(), request.getContent());
        postRepository.save(post);

        // image 생성
        for (String imgUrl : request.getImages()) {
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

        // 새로운 image 추가
        for (String imgUrl : request.getImages()) {
            PostImage postImage = new PostImage(post, imgUrl);
            postImageRepository.save(postImage);
        }

        // 제거한 image 삭제
        List<PostImage> oldImages = post.getPostImages();
        List<PostImage> editImages = request.getPostImages();

        List<PostImage> result = oldImages.stream().filter(o -> editImages.stream().noneMatch(e -> {
            return o.getId().equals(e.getId());
        })).collect(Collectors.toList());

        for (PostImage postImage : result) {
            post.getPostImages().remove(postImage);
            postImageRepository.delete(postImage);
        }

        postRepository.save(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(PostDeleteRequest request) {
        Optional<Post> postOptional = postRepository.findById(request.getPostId());
        Post post = postOptional.orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        for (PostImage postImage : post.getPostImages()) {
            postImageRepository.delete(postImage);
        }

        postRepository.delete(post);
    }
}
