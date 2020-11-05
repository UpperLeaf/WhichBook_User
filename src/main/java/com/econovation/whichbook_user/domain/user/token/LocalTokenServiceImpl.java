package com.econovation.whichbook_user.domain.user.token;

import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Date;

@Profile("test")
@Service
public class LocalTokenServiceImpl implements TokenService {
    //TODO 로컬환경에서의 TokenService이다.
    @Override
    public String createToken(JwtTokenUtils.JwtTokenType tokenType, String value) {
        if(tokenType == JwtTokenUtils.JwtTokenType.ACCESS_TOKEN) {
            return value;
        }
        else if(tokenType == JwtTokenUtils.JwtTokenType.REFRESH_TOKEN){
            return value;
        }
        throw new IllegalArgumentException("What is type of token" + tokenType);
    }

    @Override
    public void deleteToken(String token) {
        return;
    }

    @Override
    public Date getTokenExpiration(String token) {
        return new Date();
    }

    @Override
    public boolean isValidToken(String token) {
        return true;
    }

}
