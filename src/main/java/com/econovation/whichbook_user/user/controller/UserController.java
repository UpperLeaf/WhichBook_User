package com.econovation.whichbook_user.user.controller;

import com.econovation.whichbook_user.user.dto.SignUpRequestDto;
import com.econovation.whichbook_user.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public String getUserInfo(@PathVariable Long userId) {
        return userId.toString();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody SignUpRequestDto dto) {
        Long userId = userService.signUpUser(dto);
        return ResponseEntity.ok(userId);
    }


}
