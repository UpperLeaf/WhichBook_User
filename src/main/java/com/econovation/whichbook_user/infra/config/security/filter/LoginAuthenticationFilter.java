package com.econovation.whichbook_user.infra.config.security.filter;

import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.infra.config.security.handler.LoginSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, LoginSuccessHandler loginSuccessHandler, ObjectMapper objectMapper) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/user/login");
        setAuthenticationSuccessHandler(loginSuccessHandler);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        try {
            SignUpRequestDto requestDto = objectMapper.readValue(request.getInputStream(), SignUpRequestDto.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());
            setDetails(request, token);
            return getAuthenticationManager().authenticate(token);
        }catch (IOException e) {
            throw new RuntimeException("IO Exception", e);
        }
    }
}
