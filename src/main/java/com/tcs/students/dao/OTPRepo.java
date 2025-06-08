package com.tcs.students.dao;

import com.tcs.students.entity.OTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface OTPRepo extends JpaRepository<OTPEntity, Integer> {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
            "FROM OTPEntity o " +
            "WHERE o.userId = :userId AND o.otp = :otp AND o.expiredTime > CURRENT_TIMESTAMP")
    boolean isOtpValid(@Param("userId") Integer userId, @Param("otp") String otp);

    @Modifying
    @Transactional
    @Query("DELETE FROM OTPEntity o WHERE o.expiredTime <= :now")
    void deleteExpiredOtps(Date now);

}
