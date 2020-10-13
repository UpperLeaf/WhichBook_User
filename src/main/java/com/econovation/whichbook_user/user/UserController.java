package com.econovation.whichbook_user.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {

    @GetMapping("/{userId}")
    public String getUserInfo(@PathVariable Long userId) {
        return userId.toString();
    }
}
