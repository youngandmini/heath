package heavysnow.heath.dto.member;

import heavysnow.heath.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class MemberRequest {

    private String username;

    private String nickname;

    private String userStatusMessage;

    private String profileImgUrl;

    @Builder
    public MemberRequest(String username, String nickname, String userStatusMessage, String profileImgUrl) {
        this.username = username;
        this.nickname = nickname;
        this.userStatusMessage = userStatusMessage;
        this.profileImgUrl = profileImgUrl;
    }

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .nickname(nickname)
                .userStatusMessage(userStatusMessage)
                .profileImgUrl(profileImgUrl)
                .build();
    }
}
