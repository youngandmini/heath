package heavysnow.heath.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Test
    void getPostListByMember() {
        postService.getPostListByMember(1L, 2);
    }

    @Test
    void getPostList() {
        postService.getPostList(1, "createdDate");
        postService.getPostList(3, "liked");
    }
}