package com.econovation.whichbook_user.infra.config.security.handler;

import com.econovation.whichbook_user.infra.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // TODO Cookie를 사용하지 않는게 나을것 같다.
        //  쿠키 사용을 빼고 Authorization 헤더의 토큰값을 검사하는것으로 변경한다.
        Cookie[] cookies = request.getCookies();

        Arrays.stream(cookies).forEach(cookie -> {
            if(cookie.getName().equals(CookieUtils.CookieType.JWT_TOKEN.getKey())){
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }else if(cookie.getName().equals(CookieUtils.CookieType.JWT_REFRESH_TOKEN.getKey())) {
                redisTemplate.delete(cookie.getValue());
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        });

    }
}
