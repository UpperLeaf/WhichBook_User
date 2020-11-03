package com.econovation.whichbook_user.domain.user.token;

import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;

public interface TokenService {
    String createToken(JwtTokenUtils.JwtTokenType tokenType, String value);

    void updateToken();

    void deleteToken(String token);

    boolean isTokenExpired(String token);
}
