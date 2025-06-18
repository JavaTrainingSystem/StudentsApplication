package com.tcs.students.aop;

import com.tcs.students.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminSecurityAspect {

    private final CommonUtils commonUtils;

    @Pointcut("execution(* com.tcs.students.controllers.UserController.*(..))")
    public void userControllerMethods() {
    }

    @Pointcut("execution(* com.tcs.students.controllers.StorageStats.*(..))")
    public void storageControllerMethods() {
    }

    @Pointcut("execution(* com.tcs.students.controllers.DBStatsController.*(..))")
    public void dbStatsControllerMethods() {
    }

    @Before("userControllerMethods()")
    public void secureUserController() {
        commonUtils.isAdmin();
    }

    @Before("storageControllerMethods()")
    public void secureStorageController() {
        commonUtils.isAdmin();
    }

    @Before("dbStatsControllerMethods()")
    public void secureDBStatsController() {
        commonUtils.isAdmin();
    }
}
