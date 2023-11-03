package heavysnow.heath.service;

import heavysnow.heath.domain.Post;
import heavysnow.heath.dto.postdto.PostListResponseDto;
import heavysnow.heath.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


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

}
