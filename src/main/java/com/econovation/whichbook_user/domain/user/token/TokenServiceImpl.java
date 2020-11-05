package com.econovation.whichbook_user.domain.user.token;

import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Profile({"local", "prod"})
@Service
public class TokenServiceImpl implements TokenService{

    private final JwtTokenUtils jwtTokenUtils;
    private final StringRedisTemplate redisTemplate;

    public TokenServiceImpl(JwtTokenUtils jwtTokenUtils, StringRedisTemplate redisTemplate) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String createToken(JwtTokenUtils.JwtTokenType tokenType, String value) {
        String token = jwtTokenUtils.createToken(tokenType, value);
        //TODO Redis 환경 구축해야함
//        if(tokenType == JwtTokenUtils.JwtTokenType.REFRESH_TOKEN) {
//            ValueOperations<String, String> values = redisTemplate.opsForValue();
//            Duration duration = Duration.ofSeconds(jwtTokenUtils.getJwtRefreshTokenExpireLength());
//            values.set(token, value, duration);
//        }
        return token;
    }

    //TODO 로그아웃 과정으로 로그아웃시 token을 삭제한다.
    @Override
    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }

    @Override
    public Date getTokenExpiration(String token) {
        return jwtTokenUtils.getTokenExpiration(token);
    }

    @Override
    public boolean isValidToken(String token) {
        try{
            jwtTokenUtils.extractAllClaims(token);
        }catch (ExpiredJwtException | SignatureException ex){
            return false;
        }
        return true;
    }

}
