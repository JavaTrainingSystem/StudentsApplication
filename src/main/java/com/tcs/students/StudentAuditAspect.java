package com.tcs.students;

import com.tcs.students.dao.AuditRepo;
import com.tcs.students.dto.Student;
import com.tcs.students.entity.AuditEntity;
import com.tcs.students.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@RequiredArgsConstructor
public class StudentAuditAspect {

    private final AuditRepo auditRepo;

    private final CommonUtils commonUtils;

    @AfterReturning(
            value = "execution(* com.tcs.students.controllers.StudentController.saveStudent(..))",
            returning = "result"
    )
    public void auditCreateStudent(JoinPoint joinPoint, Object result) {

        Integer currentLoggedInUserId = commonUtils.getCurrentLoggedInUserId();

        auditRepo.save(AuditEntity.builder()
                .userId(currentLoggedInUserId)
                .auditType("CREATED")
                .auditDate(new Date())
                .featureName("STUDENTS")
                .build());
    }

    @Before("execution(* com.tcs.students.controllers.StudentController.saveStudent(..))")
    public void beforeSavingStudent(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof Student) {
            Student student = (Student) args[0];
            System.out.println("Before saving student: " + student.getName());
        }
    }


    @AfterReturning(
            value = "execution(* com.tcs.students.controllers.StudentController.deleteStudent(..))",
            returning = "result"
    )
    public void auditDeleteStudent(JoinPoint joinPoint, Object result) {

        Integer currentLoggedInUserId = commonUtils.getCurrentLoggedInUserId();

        auditRepo.save(AuditEntity.builder()
                .userId(currentLoggedInUserId)
                .auditType("DELETED")
                .auditDate(new Date())
                .featureName("STUDENTS")
                .build());
    }

    @AfterReturning(
            value = "execution(* com.tcs.students.controllers.StudentController.updateStudent(..))",
            returning = "result"
    )
    public void auditUpdateStudent(JoinPoint joinPoint, Object result) {

        Integer currentLoggedInUserId = commonUtils.getCurrentLoggedInUserId();

        auditRepo.save(AuditEntity.builder()
                .userId(currentLoggedInUserId)
                .auditType("UPDATED")
                .auditDate(new Date())
                .featureName("STUDENTS")
                .build());
    }

    @AfterReturning(
            value = "execution(* com.tcs.students.controllers.StudentController.uploadStudentDoc(..))",
            returning = "result"
    )
    public void auditUpdateFileStudent(JoinPoint joinPoint, Object result) {

        Integer currentLoggedInUserId = commonUtils.getCurrentLoggedInUserId();

        auditRepo.save(AuditEntity.builder()
                .userId(currentLoggedInUserId)
                .auditType("UPDATED")
                .auditDate(new Date())
                .featureName("STUDENTS")
                .build());
    }

}
