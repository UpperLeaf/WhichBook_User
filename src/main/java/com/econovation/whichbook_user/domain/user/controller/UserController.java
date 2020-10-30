package com.econovation.whichbook_user.domain.user.controller;

import com.econovation.whichbook_user.domain.user.dto.LoginRequestDto;
import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.domain.user.token.TokenService;
import com.econovation.whichbook_user.domain.user.service.UserService;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    public UserController(UserService userService, TokenService tokenService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(userId.toString());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody SignUpRequestDto signUpDto) {
        Long userId = userService.signUpUser(signUpDto);
        try {
            String json = objectMapper.writeValueAsString(userId);
            return ResponseEntity.ok(json);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginDto) {
        log.info("try login : " + loginDto.getEmail());
        if(userService.authorizeWithLoginDto(loginDto)){
            log.info("authorize success : " +  loginDto.getEmail());
            String accessToken = tokenService.createToken(JwtTokenUtils.JwtTokenType.ACCESS_TOKEN, loginDto.getEmail());
            String refreshToken = tokenService.createToken(JwtTokenUtils.JwtTokenType.REFRESH_TOKEN, loginDto.getEmail());
            try {
                Map<String, String> map = Map.of("access-Token", accessToken, "refresh-Token", refreshToken);
                String json = objectMapper.writeValueAsString(map);
                return ResponseEntity.ok(json);
            }catch (JsonProcessingException e){
                e.printStackTrace();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest servletRequest){
        String token = servletRequest.getHeader("Authentication");
        tokenService.deleteToken(token);
        log.info(token + " is deleted");
        return ResponseEntity.ok(token + " is deleted");
    }
}
