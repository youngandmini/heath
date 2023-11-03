package heavysnow.heath.dto;

import heavysnow.heath.domain.Goal;
import heavysnow.heath.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.NONE)
public class MemberDto {

    private String username;

    private String nickname;

    private String userStatusMessage;

    private String profileImgPath;

    @Builder
    public MemberDto(String username, String nickname, String userStatusMessage, String profileImgPath) {
        this.username = username;
        this.nickname = nickname;
        this.userStatusMessage = userStatusMessage;
        this.profileImgPath = profileImgPath;
    }

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .nickname(nickname)
                .userStatusMessage(userStatusMessage)
                .profileImgPath(profileImgPath)
                .build();
    }
}
