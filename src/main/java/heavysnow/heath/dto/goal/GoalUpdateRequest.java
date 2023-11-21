package heavysnow.heath.dto.goal;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GoalUpdateRequest {
    private boolean isAchieved;

    public boolean getIsAchieved() {
        return isAchieved;
    }
}
