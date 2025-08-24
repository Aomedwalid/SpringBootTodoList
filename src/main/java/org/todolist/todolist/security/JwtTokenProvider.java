package org.todolist.todolist.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.todolist.todolist.config.JwtConfig;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getJwtKey()));
    }

    public String generateAccessToken(String email){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime()+jwtConfig.getJwtAccessExpiration());


        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String getUserEmail(String token){
        return getClaims(token).getSubject();
    }

    public Boolean validateToken(String token){
        try{
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("invalid jwt Token {}" , e.getMessage());
            return false;
        }
    }

    //the repeated jwt parser
    public Claims getClaims(String token){
           return Jwts.parser()
                   .verifyWith(getSigningKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    public Long getAccessTokenExpiration(){
        return jwtConfig.getJwtAccessExpiration();
    }


}
