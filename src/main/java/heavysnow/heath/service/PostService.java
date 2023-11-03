package heavysnow.heath.service;

import heavysnow.heath.domain.Post;
import heavysnow.heath.dto.postdto.PostListResponseDto;
import heavysnow.heath.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


}
