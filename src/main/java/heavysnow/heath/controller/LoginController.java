package heavysnow.heath.controller;


import heavysnow.heath.common.LoginMemberHolder;
import heavysnow.heath.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Long login(HttpServletRequest request) {
        String token = request.getHeader("accessToken");
        loginService.login(token);
        Optional<Long> loginMemberIdOptional = LoginMemberHolder.findLoginMemberId(token);

        return loginMemberIdOptional.orElseThrow(IllegalStateException::new);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public String logout(HttpServletRequest request) {
        String token = request.getHeader("accessToken");
        loginService.logout(token);

        return "ok";
    }
}
