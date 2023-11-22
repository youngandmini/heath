package heavysnow.heath.dto.goal;

import lombok.*;

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
