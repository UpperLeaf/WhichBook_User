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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .andExpect(cookie().exists("JWT-TOKEN"))
                .andExpect(cookie().exists("JWT-REFRESH-TOKEN"));
        
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

        mockMvc.perform(get("/user/logout")
                .cookie(cookieUtils.create(CookieUtils.CookieType.JWT_TOKEN, "this is jwt cookie"))
                .cookie(cookieUtils.create(CookieUtils.CookieType.JWT_REFRESH_TOKEN, "this is refresh cookie")))
                .andExpect(cookie().exists("JWT-TOKEN"))
                .andExpect(cookie().maxAge("JWT-TOKEN", 0))
                .andExpect(cookie().exists("JWT-REFRESH-TOKEN"))
                .andExpect(cookie().maxAge("JWT-TOKEN", 0));


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
