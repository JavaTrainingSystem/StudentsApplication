package com.tcs.students.aop;

import com.tcs.students.dao.AuditRepo;
import com.tcs.students.entity.AuditEntity;
import com.tcs.students.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@RequiredArgsConstructor
public class UserAudit {

    private final AuditRepo auditRepo;

    private final CommonUtils commonUtils;

    @AfterReturning(
            value = "execution(* com.tcs.students.controllers.UserController.saveUser(..))",
            returning = "result"
    )
    public void auditCreateStudent(JoinPoint joinPoint, Object result) {

        Integer currentLoggedInUserId = commonUtils.getCurrentLoggedInUserId();

        auditRepo.save(AuditEntity.builder()
                .userId(currentLoggedInUserId)
                .auditType("CREATED")
                .auditDate(new Date())
                .featureName("USERS")
                .build());
    }


    @AfterReturning(
            value = "execution(* com.tcs.students.controllers.UserController.deleteUser(..))",
            returning = "result"
    )
    public void auditDeleteStudent(JoinPoint joinPoint, Object result) {

        Integer currentLoggedInUserId = commonUtils.getCurrentLoggedInUserId();

        auditRepo.save(AuditEntity.builder()
                .userId(currentLoggedInUserId)
                .auditType("DELETED")
                .auditDate(new Date())
                .featureName("USERS")
                .build());
    }

    @AfterReturning(
            value = "execution(* com.tcs.students.controllers.UserController.updateUser(..))",
            returning = "result"
    )
    public void auditUpdateStudent(JoinPoint joinPoint, Object result) {

        Integer currentLoggedInUserId = commonUtils.getCurrentLoggedInUserId();

        auditRepo.save(AuditEntity.builder()
                .userId(currentLoggedInUserId)
                .auditType("UPDATED")
                .auditDate(new Date())
                .featureName("USERS")
                .build());
    }

}
