package com.econovation.whichbook_user.domain.user.controller;

import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    SignUpRequestDto requestDto;

    @BeforeEach
    public void beforeEach() {
        requestDto = new SignUpRequestDto();
        requestDto.setNickname("upperleaf");
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("12345678");
        requestDto.setPasswordConfirm("12345678");
    }

    
    @DisplayName("요청 회원가입 테스트 - 성공")
    @Transactional
    @Test
    public void signUpTest() throws Exception {
        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @DisplayName("요청 회원가입 테스트 - 실패 (이메일 중복)")
    @Transactional
    @Test
    public void signUpTestWithFailureAsEmailDuplicated() throws Exception {
        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict());
    }

    @DisplayName("요청 회원가입 테스트 - 실패 (패스워드 불일치)")
    @Transactional
    @Test
    public void signUpTestWithFailureAsPasswordInCorrect() throws Exception {
        requestDto.setPasswordConfirm("12345");
        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict());
    }
    
    

}