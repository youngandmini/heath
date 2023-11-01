package heavysnow.heath.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id")
    private String id;

    private String nickname;
    private String userStatusMessage;
    private String profileImgPath;

    @OneToMany(mappedBy = "member")
    private List<Goal> goals = new ArrayList<>();
}
