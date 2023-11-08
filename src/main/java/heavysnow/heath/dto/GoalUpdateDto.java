package heavysnow.heath.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GoalUpdateDto {
    private String content;
    private boolean isAchieved;
}
