package me.yeop.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {

    /**
     * 요청값(이름, 값, 만료 기간)을 바탕으로 쿠키 추가
     *
     * @param response HttpServletResponse
     * @param name cookie name
     * @param value cookie value
     * @param maxAge 만료 기간
     */
    public static void addCookie(
            HttpServletResponse response,
            String name,
            String value,
            int maxAge
    ) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 쿠키의 이름을 입력받아 삭제
     * 실제로 삭제하는 방법은 없으므로 쿠키를 빈 값으로 바꾸고
     * 만료시간을 0으로 설정. 재생성 후 즉시 만료 처리.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param name 삭제할 cookie name
     */
    public static void deleteCookie(
            HttpServletRequest request,
            HttpServletResponse response,
            String name
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    /**
     * 객체를 직렬화해 쿠키의 값에 들어갈 값으로 변환
     *
     * @param obj cookie object
     * @return cookie string
     */
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    /**
     * 쿠키를 역직렬화 객체로 변환
     *
     * @param cookie cookie
     * @param cls 변경할 class
     * @return class
     * @param <T> class
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}
