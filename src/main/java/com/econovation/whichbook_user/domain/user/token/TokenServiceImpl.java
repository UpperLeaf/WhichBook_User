package com.econovation.whichbook_user.domain.user.token;

import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import com.econovation.whichbook_user.infra.utils.RedisUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService{

    private final JwtTokenUtils jwtTokenUtils;
    private final StringRedisTemplate redisTemplate;
    private final RedisUtils redisUtils;

    public TokenServiceImpl(JwtTokenUtils jwtTokenUtils, RedisUtils redisUtils, StringRedisTemplate redisTemplate) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.redisUtils = redisUtils;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String createToken(JwtTokenUtils.JwtTokenType tokenType, String value) {
        String token = jwtTokenUtils.createToken(tokenType, value);

        if(tokenType == JwtTokenUtils.JwtTokenType.REFRESH_TOKEN) {
            redisUtils.putData(token, value, jwtTokenUtils.getJwtRefreshTokenExpireLength());
        }
        return token;
    }

    //TODO 로그아웃 과정으로 로그아웃시 token을 삭제한다.
    @Override
    public void deleteToken(String token) {
        redisUtils.delete(token);
    }

    @Override
    public Date getTokenExpiration(String token) {
        return jwtTokenUtils.getTokenExpiration(token);
    }

    @Override
    public String getClaimValue(String token, String key) {
        return (String)jwtTokenUtils.getClaimValue(token, key);
    }

    @Override
    public boolean isValidToken(String token) {
        try{
            jwtTokenUtils.extractAllClaims(token);
        }catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException ex){
            return false;
        }
        return true;
    }

}
