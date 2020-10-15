package com.econovation.whichbook_user.infra.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class CookieUtils {

    public Cookie create(CookieType cookieType, String value){
        Cookie cookie = new Cookie(cookieType.getKey(), value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieType.getExpireTime());
        cookie.setPath("/");
        return cookie;
    }

    @Getter
    public enum CookieType {
        JWT_TOKEN("JWT-TOKEN", 1000 * 10),
        JWT_REFRESH_TOKEN("JWT-REFRESH-TOKEN", 1000 * 10 * 60);

        private String key;
        private Integer expireTime;

        CookieType(String key, Integer expireTime){
            this.key = key;
            this.expireTime = expireTime;
        }
    }
}
