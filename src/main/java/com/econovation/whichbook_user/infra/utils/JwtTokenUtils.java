package com.econovation.whichbook_user.infra.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Getter @Setter
@Component
public class JwtTokenUtils {

    @Value("${jwt.secretkey}")
    private String secretKey;

    @Value("${jwt.token.expire-length}")
    private Long jwtTokenExpireLength;

    @Value("${jwt.refresh-token.expire-length}")
    private Long jwtRefreshTokenExpireLength;



    public String createToken(CookieUtils.CookieType cookieType, String subject) {
        Claims claims = Jwts.claims().setSubject(subject);
        Date now = new Date();

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secretKey);

        if(cookieType.equals(CookieUtils.CookieType.JWT_REFRESH_TOKEN)) {
            builder.setExpiration(new Date(now.getTime() + jwtRefreshTokenExpireLength));
        }
        else if(cookieType.equals(CookieUtils.CookieType.JWT_TOKEN)) {
            builder.setExpiration(new Date(now.getTime() + jwtTokenExpireLength));
        }
        return builder.compact();
    }

    public String getSubject(String token) {
        Claims claims= extractAllClaims(token);
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        Claims claims= Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
