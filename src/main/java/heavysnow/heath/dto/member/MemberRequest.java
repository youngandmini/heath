package heavysnow.heath.dto.member;

import heavysnow.heath.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberRequest {

    private String username;

    // valid 조건 필요
    private String nickname;

    @Size(max = 20)
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
