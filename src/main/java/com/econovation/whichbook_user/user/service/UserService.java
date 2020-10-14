package com.econovation.whichbook_user.user.service;

import com.econovation.whichbook_user.user.dto.SignUpRequestDto;

public interface UserService {
    Long signUpUser(SignUpRequestDto dto);
}
