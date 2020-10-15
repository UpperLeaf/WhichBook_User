package com.econovation.whichbook_user.infra.config.security.handler;

import com.econovation.whichbook_user.domain.user.User;
import com.econovation.whichbook_user.infra.utils.CookieUtils;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenUtils jwtTokenUtils;
    private final CookieUtils cookieUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String token = jwtTokenUtils.createToken(userDetails.getUsername());
        Cookie jwtCookie = cookieUtils.create(CookieUtils.CookieType.JWT_TOKEN, token);
        response.addCookie(jwtCookie);
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
