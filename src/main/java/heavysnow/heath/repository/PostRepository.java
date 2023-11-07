package heavysnow.heath.repository;

import heavysnow.heath.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE DATE(p.createdDate) = :yesterday")
    Optional<Post> findByCreatedDate(LocalDate yesterday);


}
