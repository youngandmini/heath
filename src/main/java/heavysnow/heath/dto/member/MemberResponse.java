package heavysnow.heath.dto.member;

import heavysnow.heath.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    private Long memberId;
    private String profileImgUrl;
    private String nickname;
    private String userStatusMessage;
    private List<MemberGoalInfo> goals;

    public static MemberResponse of(Member member) {

        return new MemberResponse(
                member.getId(),
                member.getProfileImgUrl(),
                member.getNickname(),
                member.getUserStatusMessage(),
                MemberGoalInfo.listOf(member.getGoals())
        );
    }
}
