package heavysnow.heath.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginResponseDto {

    private Long memberId;

    public static LoginResponseDto of(Long memberId) {
        return new LoginResponseDto(memberId);
    }
}
