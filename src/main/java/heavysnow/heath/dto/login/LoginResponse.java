package heavysnow.heath.dto.login;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginResponse {

    private Long memberId;

    public static LoginResponse of(Long memberId) {
        return new LoginResponse(memberId);
    }
}
