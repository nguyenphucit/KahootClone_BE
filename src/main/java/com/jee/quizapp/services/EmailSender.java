package com.jee.quizapp.services;

import com.jee.quizapp.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailSender {
    @Autowired
    private JavaMailSender mailSender;
    public int sendOtpCode(String emailTo){
        SimpleMailMessage message=new SimpleMailMessage();
        int Otp=generateRandomNumber();
        Email email=new Email();
        email.setSubject("Kaphoot Verification");
        email.setBody("This is your opt code "+Otp);
        email.setEmail(emailTo);
        message.setFrom("nguyenphucit142002@gmail.com");
        message.setTo(email.getEmail());
        message.setText(email.getBody());
        message.setSubject(email.getSubject());
        try {
            mailSender.send(message);
            return Otp;
        } catch (MailException e) {
            return 0;
        }
    }
    public static int generateRandomNumber() {
        Random random = new Random();
        int min = 1000;
        int max = 9999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return randomNumber;
    }
}
