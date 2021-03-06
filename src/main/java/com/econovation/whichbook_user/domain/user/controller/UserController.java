package com.econovation.whichbook_user.domain.user.controller;

import com.econovation.whichbook_user.domain.user.dto.LoginRequestDto;
import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.domain.user.dto.SignupRequestValidator;
import com.econovation.whichbook_user.domain.user.dto.UserResponseDto;
import com.econovation.whichbook_user.domain.user.token.TokenService;
import com.econovation.whichbook_user.domain.user.service.UserService;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RequestMapping(value = "/user", produces = "application/json")
@RestController
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final SignupRequestValidator validator;

    public UserController(UserService userService, TokenService tokenService, SignupRequestValidator validator) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.validator = validator;
    }

    @InitBinder("signUpRequestDto")
    public void signUpRequestInitBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(validator);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest httpServletRequest) {
        UserResponseDto responseDto = userService.getUserByToken(httpServletRequest.getHeader("Authorization"));
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserName(@PathVariable Long id) {
        String nickname = userService.getUserNickname(id);
        return ResponseEntity.ok(nickname);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody @Validated SignUpRequestDto signUpDto, Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors.getFieldError());
        }
        Long userId = userService.createUser(signUpDto);
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginDto) {
        log.info("try login : " + loginDto.getEmail());
        userService.authorizeWithLoginDto(loginDto);
        log.info("authorize Success : " + loginDto.getEmail());
        String accessToken = tokenService.createToken(JwtTokenUtils.JwtTokenType.ACCESS_TOKEN, loginDto.getEmail());
        String refreshToken = tokenService.createToken(JwtTokenUtils.JwtTokenType.REFRESH_TOKEN, loginDto.getEmail());
        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest servletRequest){
        String token = servletRequest.getHeader("Authorization");
        tokenService.deleteToken(token);
        log.info(token + " is deleted");
        return ResponseEntity.ok(token + " is deleted");
    }
}
