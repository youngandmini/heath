package heavysnow.heath.controller;


import heavysnow.heath.common.CookieManager;
import heavysnow.heath.dto.login.LoginResponse;
import heavysnow.heath.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(HttpServletRequest request, HttpServletResponse response) {
        log.info("새로운 로그인 요청 발생");
        String token = request.getHeader("accessToken");
        log.info("새로운 로그인 요청자 token: {}", token);

        LoginResponse loginResponse = loginService.login(token);
        log.info("새로운 로그인 요청 수락. 로그인 성공: {}", token);

//        Cookie cookie = new Cookie("accessToken", token);
//        response.addCookie(cookie);

        response.setHeader("Set-Cookie", "loginSession=" + token + "; HttpOnly; SameSite=None");
        return loginResponse;
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request) {
        log.info("로그아웃 요청 발생.");
        String token = CookieManager.findLoginSessionCookie(request);
        loginService.logout(token);
        log.info("로그아웃 요청 수락.");
    }
}
