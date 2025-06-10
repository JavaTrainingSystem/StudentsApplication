package com.tcs.students.service.impl;

import com.tcs.students.EmailUtils;
import com.tcs.students.constants.CommonConstants;
import com.tcs.students.dao.OTPRepo;
import com.tcs.students.dao.UserRepo;
import com.tcs.students.dto.APIResponse;
import com.tcs.students.dto.EmailPayload;
import com.tcs.students.dto.TokenResponse;
import com.tcs.students.entity.OTPEntity;
import com.tcs.students.entity.UserEntity;
import com.tcs.students.service.LoginService;
import com.tcs.students.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private OTPRepo otpRepo;

    @Override
    public APIResponse login(String userName, String password) {

        UserEntity user = userRepo.findByUserName(userName);

        if (user == null || !user.getPassword().equalsIgnoreCase(password)) {
            return new APIResponse(CommonConstants.FAILED, 401, "User Cred's invalid");
        }

        String token;

        if (CommonConstants.YES.equals(user.getMfaEnabled())) {

            String otp = generateOtp();

            OTPEntity entity = new OTPEntity();

            Date now = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.MINUTE, 5);

            entity.setCreatedTime(now);

            entity.setOtp(otp);

            entity.setUser(user);

            entity.setExpiredTime(calendar.getTime());

            otpRepo.save(entity);

            emailUtils.sendEmail(new EmailPayload(Arrays.asList(user.getEmail()),
                    loadOtpTemplate(otp), "Login Alert!!!"));

            token = jwtUtils.createMFAToken(userName);

        } else {
            token = jwtUtils.createToken(userName);
        }

        TokenResponse tokenResponse = new TokenResponse(CommonConstants.SUCCESS, 200, "Loggedin Successfulyy", token);

        tokenResponse.setMFAEnabled(CommonConstants.YES.equals(user.getMfaEnabled()));

        return tokenResponse;
    }

    @Override
    public APIResponse verifyOtp(String token, String otp) {

        String mfaLoggedUserName = jwtUtils.getMFALoggedUserName(token);

        UserEntity userEntity = userRepo.findByUserName(mfaLoggedUserName);

        if (otpRepo.isOtpValid(userEntity.getUserId(), otp)) {
            TokenResponse tokenResponse = new TokenResponse(CommonConstants.SUCCESS, 200,
                    "Loggedin Successfulyy", jwtUtils.createToken(mfaLoggedUserName));

            return tokenResponse;
        } else {
            return new APIResponse(CommonConstants.FAILED,
                    401, "OTP Invalid");
        }
    }


    public static String loadOtpTemplate(String otpCode) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/otp-template.html");
            String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return html.replace("{{OTP_CODE}}", otpCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        int number = 100000 + random.nextInt(900000); // ensures 6 digits
        return String.valueOf(number);
    }

}

