package heavysnow.heath.service;

import heavysnow.heath.domain.Post;
import heavysnow.heath.dto.postdto.PostListResponseDto;
import heavysnow.heath.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostListResponseDto getPostListByMember(Long memberId, Pageable pageable) {
        Page<Post> postPage = postRepository.findPageByMember(memberId, pageable);
        return PostListResponseDto.of(postPage);
    }

    /**
     * 메인페이지에 사용되는 게시글 리스트를 가져오는 메서드
     * @param page: 페이지 넘버
     * @param sort: 정렬할 컬럼
     * @return PostListResponseDto
     */
    public PostListResponseDto getPostList(int page, String sort) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, sort));
        Page<Post> postPage = postRepository.findPage(pageable);
        return PostListResponseDto.of(postPage);
    }

}
