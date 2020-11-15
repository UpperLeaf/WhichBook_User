package com.econovation.whichbook_user.domain.auth;

import com.econovation.whichbook_user.domain.exception.UnAuthorizedException;
import com.econovation.whichbook_user.domain.user.token.TokenService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class HttpHeaderAuthPolicy implements AuthPolicy{

    private final TokenService tokenService;

    public HttpHeaderAuthPolicy(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean authorize(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization");
        return authorize(token);
    }

    @Override
    public boolean authorize(String token) throws UnAuthorizedException {
        if (token == null || !tokenService.isValidToken(token)){
            throw new UnAuthorizedException("토큰이 유효하지 않습니다.");
        }
        return true;
    }
}
