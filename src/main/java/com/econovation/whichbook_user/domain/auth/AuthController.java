package com.econovation.whichbook_user.domain.auth;

import com.econovation.whichbook_user.domain.user.token.TokenService;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class AuthController {

    private final TokenService tokenService;
    private final AuthPolicy authPolicy;

    public AuthController(TokenService tokenService, AuthPolicy authPolicy) {
        this.tokenService = tokenService;
        this.authPolicy = authPolicy;
    }

    @GetMapping("/auth")
    public ResponseEntity<?> authorizeWithToken(HttpServletRequest servletRequest) {
        authPolicy.authorize(servletRequest);
        return ResponseEntity.ok("Authorize Success");
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest servletRequest) {
        authPolicy.authorize(servletRequest);

        String email = tokenService.getClaimValue(servletRequest.getHeader("Authorization"), "email");
        String accessToken = tokenService.createToken(JwtTokenUtils.JwtTokenType.ACCESS_TOKEN, email);

        return new ResponseEntity<>(Map.of("accessToken", accessToken), HttpStatus.OK);
    }

}
