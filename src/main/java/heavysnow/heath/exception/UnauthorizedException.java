package heavysnow.heath.exception;

/**
 * accessToken 헤더가 null 이거나, 해당 토큰으로 사용자를 식별할 수 없을 때(로그인이 되지 않은 사용자일때)
 */
public class UnauthorizedException extends RuntimeException {

}
