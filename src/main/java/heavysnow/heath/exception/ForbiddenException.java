package heavysnow.heath.exception;

/**
 * 로그인은 되어서 멤버를 식별하였으나,해당 멤버가 해당 요청을 수행할 권한이 없을 떄
 * ex) 타인의 게시글을 삭제하는 등
 */
public class ForbiddenException extends RuntimeException {

}
