package com.econovation.whichbook_user.infra.config.security.handler;

import com.econovation.whichbook_user.infra.utils.CookieUtils;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenUtils jwtTokenUtils;
    private final CookieUtils cookieUtils;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String username = userDetails.getUsername();

        String token = jwtTokenUtils.createToken(CookieUtils.CookieType.JWT_TOKEN, username);
        Cookie jwtCookie = cookieUtils.create(CookieUtils.CookieType.JWT_TOKEN, token);

        String refreshToken = jwtTokenUtils.createToken(CookieUtils.CookieType.JWT_REFRESH_TOKEN, username);
        Cookie jwtRefreshCookie = cookieUtils.create(CookieUtils.CookieType.JWT_REFRESH_TOKEN, refreshToken);

        ValueOperations<String, String> values = redisTemplate.opsForValue();
        Duration duration = Duration.ofSeconds(jwtTokenUtils.getJwtRefreshTokenExpireLength());
        values.set(refreshToken, username, duration);

        response.addCookie(jwtCookie);
        response.addCookie(jwtRefreshCookie);
        
        redirectPrevPage(request, response, authentication);
    }

    private void redirectPrevPage(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session =  request.getSession();
        if(session != null) {
            String redirectUrl = (String) session.getAttribute("prevPage");
            if(redirectUrl != null) {
                session.removeAttribute("prevPage");
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            }else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        }else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
