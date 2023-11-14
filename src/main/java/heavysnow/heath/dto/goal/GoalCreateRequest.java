package heavysnow.heath.dto.goal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class GoalCreateRequest {
    private String content;
    private boolean isAchieved;
}
