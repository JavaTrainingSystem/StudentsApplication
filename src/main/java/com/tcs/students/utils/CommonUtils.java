package com.tcs.students.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class CommonUtils {

    public String getCurrentLoggedInUserName() {
        String authorization = getCurrentRequest().getHeader("Authorization");
        if (authorization.contains("Bearer ")) {
            authorization = authorization.replace("Bearer ", "").trim();
        }

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(JWTUtils.SECRECT_KEY)  // use the same SecretKey object used for signing
                .build()
                .parseClaimsJws(authorization);

        return claimsJws.getBody().getSubject();
    }

    public HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

}
