package heavysnow.heath.service;

import heavysnow.heath.domain.Member;
import heavysnow.heath.domain.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
class LikedServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    LikedService likedService;

    @Test
    void changeMemberPostLiked() {
        Member member = new Member("멤버", "닉네임", null, null);
        Post post = new Post();

        em.persist(member);
        em.persist(post);
        em.flush();
        em.clear();

        likedService.changeMemberPostLiked(member.getId(), post.getId());
        likedService.changeMemberPostLiked(member.getId(), post.getId());
    }
}