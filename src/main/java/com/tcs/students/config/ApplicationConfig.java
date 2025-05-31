package com.tcs.students.config;

import com.tcs.students.dao.UserRepo;
import com.tcs.students.entity.UserEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Autowired
    private UserRepo userRepo;

    @PostConstruct
    public void init() {
        UserEntity admin = userRepo.findByUserName("admin");
        if (admin == null) {
            UserEntity user = new UserEntity();
            user.setUserName("admin");
            user.setPassword("admin");
            userRepo.save(user);
        }
    }

}
