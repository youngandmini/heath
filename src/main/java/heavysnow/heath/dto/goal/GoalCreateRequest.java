package heavysnow.heath.dto.goal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GoalCreateRequest {
    private String content;
    private boolean isAchieved;

    public String getContent() {
        return content;
    }

    public boolean getIsAchieved() {
        return isAchieved;
    }
}
