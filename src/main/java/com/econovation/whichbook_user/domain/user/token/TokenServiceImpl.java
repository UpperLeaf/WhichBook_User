package com.econovation.whichbook_user.domain.user.token;

import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Profile("prod")
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

        if(tokenType == JwtTokenUtils.JwtTokenType.REFRESH_TOKEN) {
            ValueOperations<String, String> values = redisTemplate.opsForValue();
            Duration duration = Duration.ofSeconds(jwtTokenUtils.getJwtRefreshTokenExpireLength());
            values.set(token, value, duration);
        }
        return token;
    }

    //TODO 로그아웃 과정으로 로그아웃시 token을 삭제한다.
    @Override
    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return jwtTokenUtils.isTokenExpired(token);
    }

    //TODO Refresh Token을 갱신해야할때가 있다면 해당 메서드를 이용해서 갱신하자.
    @Override
    public void updateToken() {

    }
}
