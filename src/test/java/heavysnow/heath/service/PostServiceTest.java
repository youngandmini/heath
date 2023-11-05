package heavysnow.heath.service;

import heavysnow.heath.domain.Post;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    EntityManager em;

    @Test
    void getPostListByMember() {
        postService.getPostListByMember(1L, 2);
    }

    @Test
    void getPostList() {
        postService.getPostList(1, "createdDate");
        postService.getPostList(3, "liked");
    }

    @Test
    void getPostWithDetail() {
        Post post = new Post();
        em.persist(post);
        Long postId = post.getId();
        em.flush();
        em.clear();

        Assertions.assertThatThrownBy(() -> postService.getPostWithDetail(postId, 1L))
                .isInstanceOf(NullPointerException.class);
    }
}