package com.econovation.whichbook_user;

import com.econovation.whichbook_user.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class LoginTest {

    @Autowired
    UserService userServiceImpl;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Transactional
    @DisplayName("로그인 테스트")
    @Test
    public void loginTest() throws Exception {

        SignUpRequestDto requestDto = new SignUpRequestDto();
        requestDto.setUsername("upperleaf");
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("12345678");

        userServiceImpl.signUpUser(requestDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@email.com");
        loginDto.setPassword("12345678");

        String json = objectMapper.writeValueAsString(loginDto);

        mockMvc.perform(post("/user/login")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(cookie().exists("TEST_TOKEN_ABCD"));
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
