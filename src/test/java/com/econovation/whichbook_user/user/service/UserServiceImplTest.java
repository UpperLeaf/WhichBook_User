package com.econovation.whichbook_user.user.service;

import com.econovation.whichbook_user.user.User;
import com.econovation.whichbook_user.user.UserRepository;
import com.econovation.whichbook_user.user.dto.SignUpRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class UserServiceImplTest {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserRepository userRepository;

    SignUpRequestDto requestDto;

    @BeforeEach
    public void beforeEach() {
        requestDto = new SignUpRequestDto();
        requestDto.setUsername("upperleaf");
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("12345678");
    }


    @Transactional
    @DisplayName("회원가입 성공")
    @Test
    public void signUpWithSuccess() {
        Long id = userService.signUpUser(requestDto);

        User user = userRepository.findById(id).get();

        assertThat(requestDto.getEmail(), is(user.getEmail()));
        assertThat(requestDto.getUsername(), is(user.getName()));
        assertThat(requestDto.getPassword(), is(not((user.getPassword()))));
    }

    @Transactional
    @DisplayName("UserDetails loadByUsername 테스트")
    @Test
    public void loadByUsernameTest() {
        Long id = userService.signUpUser(requestDto);

        UserDetails userDetails = userService.loadUserByUsername(requestDto.getEmail());

        assertThat(userDetails.getUsername(), is(requestDto.getEmail()));
        assertThat(userDetails.getPassword(), is(not(requestDto.getPassword())));
        assertTrue(userDetails.isEnabled());
    }


}