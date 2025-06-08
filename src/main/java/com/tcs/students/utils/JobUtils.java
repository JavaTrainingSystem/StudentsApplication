package com.tcs.students.utils;

import com.tcs.students.dao.OTPRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class JobUtils {

    @Autowired
    private OTPRepo otpRepo;


    @Scheduled(cron = "0 */1 * * * ?")
    public void deleteExpiredOTP() {

        Date date = new Date();

        otpRepo.deleteExpiredOtps(date);

        System.out.println("Deleting expired otp " + date);
    }

}
