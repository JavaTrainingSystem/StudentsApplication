package com.tcs.students.service;

import com.tcs.students.dto.APIResponse;

public interface LoginService {

    APIResponse login(String userName, String password);

    APIResponse verifyOtp(String token, String otp);
}
