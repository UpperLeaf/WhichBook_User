package com.econovation.whichbook_user.domain.user.dto;

import com.econovation.whichbook_user.domain.user.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class SignUpRequestDto {
    private String email;
    private String nickname;
    private String password;
    private String passwordConfirm;

    public User toUser(PasswordEncoder encoder) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(encoder.encode(password))
                .build();
    }
}
