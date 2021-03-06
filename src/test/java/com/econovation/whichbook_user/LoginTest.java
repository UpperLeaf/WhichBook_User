package com.econovation.whichbook_user;

import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.domain.user.service.UserService;
import com.econovation.whichbook_user.domain.user.token.TokenService;
import com.econovation.whichbook_user.infra.utils.CookieUtils;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class LoginTest {

    @Autowired MockMvc mockMvc;

    @Autowired UserService userServiceImpl;
    @Autowired ObjectMapper objectMapper;
    @Autowired TokenService tokenService;

    @Transactional
    @DisplayName("로그인 테스트 - 성공")
    @Test
    public void loginTest() throws Exception {
        SignUpRequestDto requestDto = getSignUpRequestDto();
        userServiceImpl.createUser(requestDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@email.com");
        loginDto.setPassword("12345678");

        String json = objectMapper.writeValueAsString(loginDto);

        mockMvc.perform(post("/user/login")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(jsonPath("refreshToken").exists());
    }

    @Transactional
    @DisplayName("로그인 테스트 - 실패")
    @Test
    public void loginTestWithFailure() throws Exception {

        SignUpRequestDto requestDto = getSignUpRequestDto();

        userServiceImpl.createUser(requestDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@email.com");
        loginDto.setPassword("1234567");

        String json = objectMapper.writeValueAsString(loginDto);

        mockMvc.perform(post("/user/login")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Transactional
    @DisplayName("로그아웃 테스트 - 성공")
    @Test
    public void logoutTestWithSuccess() throws Exception {
        //TODO 로그아웃시 Token을 만료시킬 수 없다. Logout로직을 작성해야한다.
        // 또한 로그인했을때 Redis에 저장되어있을 Refresh-Token을, 로그아웃시에 삭제한다.
        String token = tokenService.createToken(JwtTokenUtils.JwtTokenType.REFRESH_TOKEN, "test@email.com");

        mockMvc.perform(get("/user/logout")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string(token + " is deleted"));

    }


    private SignUpRequestDto getSignUpRequestDto() {
        SignUpRequestDto requestDto = new SignUpRequestDto();
        requestDto.setNickname("upperleaf");
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("12345678");
        return requestDto;
    }


    public static class LoginDto {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
