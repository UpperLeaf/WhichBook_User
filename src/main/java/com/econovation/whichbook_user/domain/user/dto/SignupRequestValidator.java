package com.econovation.whichbook_user.domain.user.dto;

import com.econovation.whichbook_user.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class SignupRequestValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpRequestDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpRequestDto signUpRequestDto = (SignUpRequestDto)target;

        if(userService.existByEmail(signUpRequestDto.getEmail())) {
            errors.rejectValue("email", "이미 존재하는 이메일 입니다.");
        }

        if(!signUpRequestDto.getPassword().equals(signUpRequestDto.getPasswordConfirm())){
            errors.rejectValue("passwordConfirm", "패스워드 확인이 일치하지 않습니다.");
        }
    }
}
