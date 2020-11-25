package com.econovation.whichbook_user.domain.user.service;

import com.econovation.whichbook_user.domain.user.dto.LoginRequestDto;
import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.domain.user.dto.UserResponseDto;


public interface UserService {

    boolean existByEmail(String email);

    UserResponseDto getUserByToken(String token);

    Long createUser(SignUpRequestDto dto);

    boolean authorizeWithLoginDto(LoginRequestDto loginDto);
}
