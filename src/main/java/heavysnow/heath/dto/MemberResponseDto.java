package heavysnow.heath.dto;

import heavysnow.heath.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponseDto {

    private Long memberId;
    private String profileImgUrl;
    private String nickname;
    private String userStatusMessage;
    private List<MemberGoalInfo> goals;

    public static MemberResponseDto of(Member member) {

        return new MemberResponseDto(
                member.getId(),
                member.getProfileImgUrl(),
                member.getNickname(),
                member.getUserStatusMessage(),
                MemberGoalInfo.listOf(member.getGoals())
        );
    }
}
