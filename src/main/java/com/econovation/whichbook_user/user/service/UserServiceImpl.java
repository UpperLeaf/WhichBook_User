package com.econovation.whichbook_user.user.service;

import com.econovation.whichbook_user.user.User;
import com.econovation.whichbook_user.user.UserRepository;
import com.econovation.whichbook_user.user.dto.SignUpRequestDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
    }

    @Override
    public Long signUpUser(SignUpRequestDto dto) {
        User user = dto.toUser(passwordEncoder);
        return userRepository.save(user).getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "에 해당하는 유저가 없습니다"));

        return user;
    }
}
