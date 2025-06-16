package com.tcs.students.utils;

import com.tcs.students.dao.UserRepo;
import com.tcs.students.entity.UserEntity;
import com.tcs.students.exceptions.handlers.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class CommonUtils {

    private final UserRepo userRepo;

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

    public Integer getCurrentLoggedInUserId() {
        UserEntity userEntity = userRepo.findByUserName(getCurrentLoggedInUserName());
        return userEntity.getUserId();
    }

    public void isAdmin() {
        String currentLoggedInUserName = getCurrentLoggedInUserName();

        if (!"admin".equalsIgnoreCase(currentLoggedInUserName))
            throw new UnAuthorizedException("UnAuthorized Access");
    }

}
