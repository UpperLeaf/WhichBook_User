package com.econovation.whichbook_user.domain.auth;

import com.econovation.whichbook_user.domain.user.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.security.CodeSigner;
import java.util.Arrays;
import java.util.stream.IntStream;

@Aspect
@Component
public class AuthorizeAop {

    private final AuthPolicy authPolicy;
    private final UserService userService;

    public AuthorizeAop(AuthPolicy authPolicy, UserService userService) {
        this.authPolicy = authPolicy;
        this.userService = userService;
    }

    @Around("@annotation(IsAuthenticated)")
    public Object checkAuthority(ProceedingJoinPoint joinPoint) throws Throwable {
        CodeSignature codeSignature = (CodeSignature)joinPoint.getSignature();
        for(int i = 0; i < codeSignature.getParameterNames().length; i++){
            if(codeSignature.getParameterNames()[i].equals("token") && authPolicy.authorize((String)joinPoint.getArgs()[i])){
                return joinPoint.proceed();
            }
        }
        return null;
    }

}
