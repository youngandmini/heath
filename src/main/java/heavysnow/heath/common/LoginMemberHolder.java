package heavysnow.heath.common;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Component
public class LoginMemberHolder {

    /**
     * 현재 로그인한 멤버의 정보를 토큰 - memberId 형태로 담음
     * 사용하지 않은지 30분이 지나면 자동으로 만료되도록 설정
     */
    private static final ExpiringMap<String, Long> loginMembers = ExpiringMap.builder()
            .maxSize(500)
            .expirationPolicy(ExpirationPolicy.ACCESSED)
            .expiration(30, TimeUnit.MINUTES)
            .build();

    /**
     * 헤더의 토큰 정보를 가지고 로그인한 멤버의 id를 찾음
     * @param token
     * @return
     */
    public static Optional<Long> findLoginMemberId(String token) {
        if (token == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(loginMembers.get(token));
    }

    public void login(String token, Long memberId) {
        loginMembers.put(token, memberId);
    }

    public void logout(String token) {
        loginMembers.remove(token);
    }
}
