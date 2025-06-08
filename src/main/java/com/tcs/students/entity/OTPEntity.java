package com.tcs.students.entity;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "otps")
public class OTPEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private Integer otpId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "otp")
    private String otp;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "expired_time")
    private Date expiredTime;

    public Integer getOtpId() {
        return otpId;
    }

    public void setOtpId(Integer otpId) {
        this.otpId = otpId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }
}
