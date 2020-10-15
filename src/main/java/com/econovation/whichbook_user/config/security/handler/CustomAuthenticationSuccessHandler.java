package com.econovation.whichbook_user.config.security.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        Cookie cookie = new Cookie("TEST_TOKEN_ABCD", "TEST TOKEN VALUE");
        response.addCookie(cookie);

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
