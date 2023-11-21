package heavysnow.heath.controller;


import heavysnow.heath.dto.login.LoginResponse;
import heavysnow.heath.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
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
    public LoginResponse login(HttpServletRequest request) {
        log.info("새로운 로그인 요청");
        String token = request.getHeader("accessToken");
        log.info("token: {}", token);

        return loginService.login(token);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request) {
        String token = request.getHeader("accessToken");
        loginService.logout(token);
    }
}
