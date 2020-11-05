package com.econovation.whichbook_user.domain.user.token;

import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;

import java.util.Date;

public interface TokenService {
    String createToken(JwtTokenUtils.JwtTokenType tokenType, String value);

    void deleteToken(String token);

    Date getTokenExpiration(String token);

    String getClaimValue(String token, String key);

    boolean isValidToken(String token);
}
