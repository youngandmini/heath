package heavysnow.heath.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String nickname;
    private String userStatusMessage;
    private String profileImgUrl;

    @OneToMany(mappedBy = "member")
    private List<Goal> goals = new ArrayList<>();

    @Builder
    public Member(String username, String nickname, String userStatusMessage, String profileImgUrl) {
        this.username = username;
        this.nickname = nickname;
        this.userStatusMessage = userStatusMessage;
        this.profileImgUrl = profileImgUrl;
    }

    public void update(String nickname, String userStatusMessage, String profileImgUrl) {
        this.nickname = nickname;
        this.userStatusMessage = userStatusMessage;
        this.profileImgUrl = profileImgUrl;
        }


}

