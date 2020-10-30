package com.econovation.whichbook_user.domain.user.service;

import com.econovation.whichbook_user.domain.user.dto.LoginRequestDto;
import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;

public interface UserService {
    Long signUpUser(SignUpRequestDto dto);

    boolean authorizeWithLoginDto(LoginRequestDto loginDto);
}
