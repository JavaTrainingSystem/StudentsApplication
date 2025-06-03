package com.tcs.students.controllers;

import com.tcs.students.dto.APIResponse;
import com.tcs.students.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("login")
    public ResponseEntity<APIResponse> login(@RequestBody Map<String, String> payload) {
        String userName = payload.get("userName");
        String encodedPassword = payload.get("password");
        String password = new String(Base64.getDecoder().decode(encodedPassword));
        return new ResponseEntity<>(loginService.login(userName, password), HttpStatus.OK);
    }

}
