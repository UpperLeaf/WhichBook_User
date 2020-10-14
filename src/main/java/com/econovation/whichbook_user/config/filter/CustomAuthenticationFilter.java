package com.econovation.whichbook_user.config.filter;

import com.econovation.whichbook_user.user.dto.SignUpRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        setAuthenticationManager(authenticationManager);
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
