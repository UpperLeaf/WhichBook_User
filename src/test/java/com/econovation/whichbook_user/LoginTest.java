package com.econovation.whichbook_user;

import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.domain.user.service.UserService;
import com.econovation.whichbook_user.infra.utils.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
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

    @Autowired UserService userServiceImpl;
    @Autowired ObjectMapper objectMapper;
    @Autowired MockMvc mockMvc;
    @Autowired CookieUtils cookieUtils;
    @Autowired StringRedisTemplate redisTemplate;

    @Transactional
    @DisplayName("로그인 테스트 - 성공")
    @Test
    public void loginTest() throws Exception {

        SignUpRequestDto requestDto = getSignUpRequestDto();

        userServiceImpl.signUpUser(requestDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@email.com");
        loginDto.setPassword("12345678");

        String json = objectMapper.writeValueAsString(loginDto);

        mockMvc.perform(post("/user/login")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("access-Token").exists())
                .andExpect(jsonPath("access-Token").value("test@email.com"))
                .andExpect(jsonPath("refresh-Token").exists())
                .andExpect(jsonPath("refresh-Token").value("test@email.com"));


    }

    @Transactional
    @DisplayName("로그인 테스트 - 실패")
    @Test
    public void loginTestWithFailure() throws Exception {

        SignUpRequestDto requestDto = getSignUpRequestDto();

        userServiceImpl.signUpUser(requestDto);

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
        mockMvc.perform(get("/user/logout")
                .header("Authorization", "token auth value"))
                .andExpect(status().isOk())
                .andExpect(content().string("token auth value is deleted"));

    }


    private SignUpRequestDto getSignUpRequestDto() {
        SignUpRequestDto requestDto = new SignUpRequestDto();
        requestDto.setUsername("upperleaf");
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
