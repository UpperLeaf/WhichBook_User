package com.econovation.whichbook_user.domain.user.dto;

import com.econovation.whichbook_user.domain.user.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class SignUpRequestDto {
    private String email;
    private String username;
    private String password;

    public User toUser(PasswordEncoder encoder) {
        return User.builder()
                .email(email)
                .name(username)
                .password(encoder.encode(password))
                .build();
    }
}
