package com.tcs.students.controllers;

import com.tcs.students.constants.CommonConstants;
import com.tcs.students.dto.APIResponse;
import com.tcs.students.dto.TokenResponse;
import com.tcs.students.service.LoginService;
import com.tcs.students.utils.JWTUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class LoginController {

    private final LoginService loginService;

    private final JWTUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    public LoginController(LoginService loginService, JWTUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.loginService = loginService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }


//    @PostMapping("login")
//    public ResponseEntity<APIResponse> login(@RequestBody Map<String, String> payload) {
//        String userName = payload.get("userName");
//        String encodedPassword = payload.get("password");
//        String password = new String(Base64.getDecoder().decode(encodedPassword));
//        return new ResponseEntity<>(loginService.login(userName, password), HttpStatus.OK);
//    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody Map<String, String> payload) {
        String userName = payload.get("userName");
        String encodedPassword = payload.get("password");
        String password = new String(Base64.getDecoder().decode(encodedPassword));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password)
        );

        String token = jwtUtils.createToken(userName);
        TokenResponse tokenResponse = new TokenResponse(CommonConstants.SUCCESS, 200, "Loggedin Successfulyy", token);

        tokenResponse.setMFAEnabled(false);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("verify-otp")
    public ResponseEntity<APIResponse> verifyOtp(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String otp = payload.get("otp");

        if (!jwtUtils.validateMFAToken(token)) {
            return new ResponseEntity<>(new APIResponse(CommonConstants.FAILED,
                    401, "Session Expired"), HttpStatus.UNAUTHORIZED);
        }

        APIResponse apiResponse = loginService.verifyOtp(token, otp);

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatusCode()));
    }

}
