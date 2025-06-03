package com.tcs.students.service.impl;

import com.tcs.students.constants.CommonConstants;
import com.tcs.students.dao.UserRepo;
import com.tcs.students.dto.APIResponse;
import com.tcs.students.dto.TokenResponse;
import com.tcs.students.entity.UserEntity;
import com.tcs.students.service.LoginService;
import com.tcs.students.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public APIResponse login(String userName, String password) {

        UserEntity user = userRepo.findByUserName(userName);

        if (user == null || !user.getPassword().equalsIgnoreCase(password)) {
            return new APIResponse(CommonConstants.FAILED, 401, "User Cred's invalid");
        }

        String token = jwtUtils.createToken(userName);

        return new TokenResponse(CommonConstants.SUCCESS, 200, "Loggedin Successfulyy", token);
    }

}

