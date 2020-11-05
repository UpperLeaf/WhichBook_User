package com.econovation.whichbook_user.domain.user.token;

import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class TokenServiceImplTest {

    @Autowired TokenService tokenService;

    @DisplayName("토큰 생성 후 유효성 검사")
    @Test
    public void createTokenAndValidation() {
        String token = tokenService.createToken(JwtTokenUtils.JwtTokenType.ACCESS_TOKEN, "testvalue");
        assertTrue(tokenService.isValidToken(token));
    }

    @DisplayName("유효하지 않은 토큰에 대한 유효성 검사")
    @Test
    public void InvalidTokenValidation() {
        assertFalse(tokenService.isValidToken("invalidToken"));
    }
}