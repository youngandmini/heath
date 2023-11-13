package heavysnow.heath.dto.member;


import heavysnow.heath.domain.Goal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberGoalInfo {
    private Long goalId;
    private String content;
    private boolean isAchieved;

    private static MemberGoalInfo of(Goal goal) {
        return new MemberGoalInfo(
                goal.getId(),
                goal.getContent(),
                goal.isAchieved()
        );
    }

    public static List<MemberGoalInfo> listOf(List<Goal> goals) {
        return goals.stream().map(MemberGoalInfo::of).collect(Collectors.toList());
    }
}
