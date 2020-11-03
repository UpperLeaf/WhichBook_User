package com.econovation.whichbook_user.domain.auth;

import com.econovation.whichbook_user.domain.user.token.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    /*
    * TODO
    *  클라이언트에서 HTTP Header Authorization이라는 헤더에 AccessToken의 값을 포함하여 Request를 해야해.
    *  Authorization : "access-Token"
    *  이 토큰값을 검사해서 유효한 토큰이면 인증해줌. 상태코드 200반환.
    *  이 토큰값이 이상할 경우 401반환
    */
    @GetMapping("/auth")
    public ResponseEntity<?> authorizeUserGet(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization");
        if(tokenService.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok("인증됬음");

    }
}
