package com.econovation.whichbook_user.domain.user.service;

import com.econovation.whichbook_user.domain.user.User;
import com.econovation.whichbook_user.domain.user.dto.LoginRequestDto;
import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.domain.user.dto.UserResponseDto;

import java.util.Optional;

public interface UserService {

    boolean existByEmail(String email);

    Optional<UserResponseDto> getUserByToken(String token);

    Long createUser(SignUpRequestDto dto);

    boolean authorizeWithLoginDto(LoginRequestDto loginDto);
}
