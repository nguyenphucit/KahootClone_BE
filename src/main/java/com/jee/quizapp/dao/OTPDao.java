package com.jee.quizapp.dao;

import com.jee.quizapp.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPDao extends JpaRepository<OTP,Integer> {
    @Query(value="SELECT otp FROM OTP otp where otp.otpUser.id=:id")
    OTP getOtpByUserId(Integer id);
}
