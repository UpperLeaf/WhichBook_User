package com.econovation.whichbook_user.domain.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    /*
    * TODO
    *  HttpServletRequest에서  JWT-TOKEN, JWT-Refresh 토큰을 추출한다.
    *  각 추출한 토큰에서 JWT-TOKEN이 있을경우 응답으로 200 상태코드를 전송한다.
    *  
    *
    */
    @GetMapping("/auth")
    public String authorizeUserGet() {

        return "asdfasdfasd";
    }

    @PostMapping("/auth")
    public String authorizeUser() {

        return "auth";
    }
}
