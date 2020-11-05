package com.econovation.whichbook_user.domain.auth;

import com.econovation.whichbook_user.domain.user.token.TokenService;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class AuthController {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    public AuthController(TokenService tokenService, ObjectMapper objectMapper) {
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/auth")
    public ResponseEntity<?> authorizeUserGet(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization");
        if(token == null || !tokenService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok("Authorize Success");
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization");
        if(token == null || !tokenService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ReLogin Required");
        }
        String email = tokenService.getClaimValue(token, "email");
        String accessToken = tokenService.createToken(JwtTokenUtils.JwtTokenType.ACCESS_TOKEN, email);
        try {
            String json = objectMapper.writeValueAsString(Map.of("accessToken", accessToken));
            return ResponseEntity.status(HttpStatus.OK).body(json);
        }catch (JsonProcessingException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
