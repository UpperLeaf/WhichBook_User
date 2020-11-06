package com.econovation.whichbook_user.domain.user.controller;

import com.econovation.whichbook_user.domain.user.dto.LoginRequestDto;
import com.econovation.whichbook_user.domain.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.domain.user.dto.SignupRequestValidator;
import com.econovation.whichbook_user.domain.user.dto.UserResponseDto;
import com.econovation.whichbook_user.domain.user.token.TokenService;
import com.econovation.whichbook_user.domain.user.service.UserService;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/user", produces = "application/json")
@RestController
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final SignupRequestValidator validator;

    public UserController(UserService userService, TokenService tokenService, ObjectMapper objectMapper, SignupRequestValidator validator) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @InitBinder("signUpRequestDto")
    public void signUpRequestInitBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(validator);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest httpServletRequest) {
        Optional<UserResponseDto> responseDto =
                userService.getUserByToken(httpServletRequest.getHeader("Authorization"));
        if(responseDto.isPresent()){
            try {
                String json = objectMapper.writeValueAsString(responseDto.get());
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }catch (JsonProcessingException e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody @Validated SignUpRequestDto signUpDto, Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors.getFieldError().getField());
        }
        Long userId = userService.createUser(signUpDto);
        try {
            log.info("sign up user Id : " + userId);
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
                Map<String, String> map = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
                String json = objectMapper.writeValueAsString(map);
                System.out.println(json);
                return ResponseEntity.ok(json);
            }catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest servletRequest){
        String token = servletRequest.getHeader("Authorization");
        tokenService.deleteToken(token);
        log.info(token + " is deleted");
        return ResponseEntity.ok(token + " is deleted");
    }
}
