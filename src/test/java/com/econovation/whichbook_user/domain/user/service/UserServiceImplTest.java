package com.econovation.whichbook_user.domain.user.service;

import com.econovation.whichbook_user.domain.user.User;
import com.econovation.whichbook_user.domain.user.UserRepository;
import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.domain.user.dto.UserResponseDto;
import com.econovation.whichbook_user.domain.user.token.TokenService;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceImplTest {

    @Autowired UserServiceImpl userService;
    @Autowired TokenService tokenService;
    @Autowired UserRepository userRepository;

    SignUpRequestDto requestDto;

    @BeforeEach
    public void beforeEach() {
        requestDto = new SignUpRequestDto();
        requestDto.setNickname("upperleaf");
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("12345678");
    }


    @Transactional
    @DisplayName("서비스 계층 회원가입 성공")
    @Test
    public void signUpWithSuccess() {
        Long id = userService.createUser(requestDto);

        User user = userRepository.findById(id).get();

        assertThat(requestDto.getEmail(), is(user.getEmail()));
        assertThat(requestDto.getNickname(), is(user.getNickname()));
        assertThat(requestDto.getPassword(), is(not((user.getPassword()))));
    }

    @Transactional
    @DisplayName("IsAuthenticated Aspect 동작되는지 테스트 - 유효한 토큰")
    @Test
    public void checkIsAuthenticatedAspectWithValidToken() {
        String email = requestDto.getEmail();

        userService.createUser(requestDto);

        String token = tokenService.createToken(JwtTokenUtils.JwtTokenType.ACCESS_TOKEN, email);
        UserResponseDto user = userService.getUserByToken(token).get();
        assertEquals(user.getEmail(), email);
    }

    @Transactional
    @DisplayName("IsAuthenticated Aspect 동작되는지 테스트 - 유효하지 않은 토큰")
    @Test
    public void checkIsAuthenticatedAspectWithInValidToken() {
        String email = requestDto.getEmail();

        userService.createUser(requestDto);

        Assertions.assertThrows(NullPointerException.class, () -> {
            String token = tokenService.createToken(JwtTokenUtils.JwtTokenType.ACCESS_TOKEN, email);
            token += "abcd";
            UserResponseDto user = userService.getUserByToken(token).get();
        });
    }

    @Transactional
    @DisplayName("UserDetails loadByUsername 테스트")
    @Test
    public void loadByUsernameTest() {
        Long id = userService.createUser(requestDto);

        UserDetails userDetails = userService.loadUserByUsername(requestDto.getEmail());

        assertThat(userDetails.getUsername(), is(requestDto.getEmail()));
        assertThat(userDetails.getPassword(), is(not(requestDto.getPassword())));
        assertTrue(userDetails.isEnabled());
    }


}