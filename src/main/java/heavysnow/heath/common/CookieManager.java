package heavysnow.heath.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class CookieManager {

    public static String findLoginSessionCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        Cookie accessToken = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("loginSession"))
                .findAny().orElse(null);
        if (accessToken == null) {
            return null;
        }
        return accessToken.getValue();
    }
}
