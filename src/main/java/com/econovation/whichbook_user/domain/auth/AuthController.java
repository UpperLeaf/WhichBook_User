package com.econovation.whichbook_user.domain.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/auth")
    public String authorizeUserGet() {

        return "asdfasdfasd";
    }

    @PostMapping("/auth")
    public String authorizeUser() {

        return "auth";
    }
}
