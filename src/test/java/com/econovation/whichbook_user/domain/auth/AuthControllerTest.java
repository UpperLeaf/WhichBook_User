package com.econovation.whichbook_user.domain.auth;

import com.econovation.whichbook_user.domain.user.token.TokenService;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @DisplayName("유효한 토큰으로 권한 검사")
    @Test
    void authorizeWithValidToken() throws Exception {
        String accessToken = tokenService.createToken(JwtTokenUtils.JwtTokenType.ACCESS_TOKEN, "test@email.com");

        mockMvc.perform(get("/auth").header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Authorize Success"));

    }

    @DisplayName("유효하지 않은 토큰으로 권한 검사")
    @Test
    void authorizeWithInValidToken() throws Exception {

        mockMvc.perform(get("/auth").header("Authorization", "InValidToken"))
                .andExpect(status().isUnauthorized());

    }
}