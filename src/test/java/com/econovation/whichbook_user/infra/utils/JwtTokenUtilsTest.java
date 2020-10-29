package com.econovation.whichbook_user.infra.utils;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenUtilsTest {

    @Autowired
    JwtTokenUtils utils;

    @DisplayName("properties 값 주입이 제대로 되었는지 테스트")
    @Test
    public void testValueOfSecretKey() {
        assertNotNull(utils.getSecretKey());
    }


    @DisplayName("토큰 생성 및 Parse 테스트")
    @Test
    public void createTokenTest() {
        String token = utils.createToken(CookieUtils.CookieType.JWT_TOKEN, "test@email.com");
        String subject = utils.getSubject(token);
        System.out.println(token);
        assertTrue(Jwts.parser().isSigned(token));
        assertEquals("test@email.com", subject);
    }

}