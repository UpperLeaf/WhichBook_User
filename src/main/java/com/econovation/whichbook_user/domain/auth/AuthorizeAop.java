package com.econovation.whichbook_user.domain.auth;

import com.econovation.whichbook_user.domain.exception.UnAuthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizeAop {

    private final AuthPolicy authPolicy;

    public AuthorizeAop(AuthPolicy authPolicy) {
        this.authPolicy = authPolicy;
    }

    @Around("@annotation(IsAuthenticated)")
    public Object checkAuthority(ProceedingJoinPoint joinPoint) throws Throwable {
        CodeSignature codeSignature = (CodeSignature)joinPoint.getSignature();
        for(int i = 0; i < codeSignature.getParameterNames().length; i++){
            if(codeSignature.getParameterNames()[i].equals("token") && authPolicy.authorize((String)joinPoint.getArgs()[i])){
                return joinPoint.proceed();
            }
        }
        throw new UnAuthorizedException("토큰이 유효하지 않습니다.");
    }
}
