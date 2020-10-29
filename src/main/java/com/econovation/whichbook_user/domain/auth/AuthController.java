package com.econovation.whichbook_user.domain.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    @GetMapping("/auth")
    public String authorizeUserGet() {
        //TODO auth 요청시 Token 또는 Refresh Token을 확인하여 인증할것.
        return "asdfasdfasd";
    }

    @PostMapping("/auth")
    public String authorizeUser() {

        return "auth";
    }
}
