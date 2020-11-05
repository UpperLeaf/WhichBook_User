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

    public enum JwtTokenType{
        ACCESS_TOKEN("ACCESS-TOKEN"),
        REFRESH_TOKEN("REFRESH-TOKEN");

        String name;
        JwtTokenType(String name) {
            this.name = name;
        }
    }

    public String createToken(JwtTokenType jwtTokenType, String value) {
        Claims claims = Jwts.claims();
        Date now = new Date();

        if(jwtTokenType == JwtTokenType.ACCESS_TOKEN){
            claims.setSubject("Access-Token");
            claims.setExpiration(new Date(now.getTime() + jwtTokenExpireLength));

            claims.setIssuedAt(now);
            claims.put("email", value);
            claims.put("expiration", jwtTokenExpireLength);
            claims.setIssuer("whichbook_user_service");
        }else if(jwtTokenType == JwtTokenType.REFRESH_TOKEN) {
            claims.setSubject("Refresh-Token");
            claims.setExpiration(new Date(now.getTime() + jwtRefreshTokenExpireLength));
            claims.setIssuedAt(now);
            claims.put("email", value);
            claims.put("expiration", jwtRefreshTokenExpireLength);
            claims.setIssuer("whichbook_user_service");
        }

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey);

        return builder.compact();
    }

    public String getSubject(String token) {
        Claims claims= extractAllClaims(token);
        return claims.getSubject();
    }

    public Object getClaimValue(String token, String key) {
        Claims claims = extractAllClaims(token);
        return claims.get(key);
    }

    public Date getTokenExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    public Claims extractAllClaims(String token) {
        Claims claims= Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
