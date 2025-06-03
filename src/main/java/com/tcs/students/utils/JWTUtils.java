package com.tcs.students.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtils {

    private final String SECRECT_KEY = "kxbS8M/cCKlZJCwadZ02tYJwS9U094P/J8J5eB9tPKM0wqg63N4yvBDhkY3CBGPD9FEitkZXxra7yqLgd5m+SA==";

    private SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(SECRECT_KEY), SignatureAlgorithm.HS512.getJcaName());

    private final long expireAfterMillis = 10 * 60 * 1000;


    public String createToken(String userName) {
        return Jwts.builder().setSubject(userName)
                .setIssuer("student-management")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireAfterMillis))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    public boolean validateToken(String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }

        try {
            token = token.replace("Bearer ", "");

            Jwts.parserBuilder()
                    .setSigningKey(key)  // use the same SecretKey object used for signing
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }

}
