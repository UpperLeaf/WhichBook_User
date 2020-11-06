package com.econovation.whichbook_user.domain.user.service;

import com.econovation.whichbook_user.domain.auth.IsAuthenticated;
import com.econovation.whichbook_user.domain.user.User;
import com.econovation.whichbook_user.domain.user.UserRepository;
import com.econovation.whichbook_user.domain.user.dto.LoginRequestDto;
import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.domain.user.dto.UserResponseDto;
import com.econovation.whichbook_user.domain.user.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, TokenService tokenService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = encoder;
    }


    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @IsAuthenticated
    @Override
    public Optional<UserResponseDto> getUserByToken(String token) {
        String email = tokenService.getClaimValue(token, "email");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당하는 Email이 없습니다"));
        return Optional.of(UserResponseDto.of(user));
    }

    @Override
    public Long createUser(SignUpRequestDto dto) {
        User user = dto.toUser(passwordEncoder);
        return userRepository.save(user).getId();
    }

    @Override
    public boolean authorizeWithLoginDto(LoginRequestDto loginDto) {
        String userEmail = loginDto.getEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException(userEmail + "에 해당하는 이메일이 없습니다."));
        return passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "에 해당하는 유저가 없습니다"));
        return user;
    }
}
