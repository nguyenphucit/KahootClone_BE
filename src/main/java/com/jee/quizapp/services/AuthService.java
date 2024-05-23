package com.jee.quizapp.services;

import com.jee.quizapp.dao.OTPDao;
import com.jee.quizapp.dao.UserDao;
import com.jee.quizapp.model.OTP;
import com.jee.quizapp.model.ResponseObject;
import com.jee.quizapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    UserDao userDao;
    @Autowired
    OTPDao otpDao;
    @Autowired
    EmailSender emailSender;

    public ResponseEntity<ResponseObject> login(User user){
        // tìm user ứng với email được gửi
        User userEmail=userDao.findUserByEmail(user.getEmail());
        // nếu có user
        if(userEmail!=null) {
            // tìm user ứng với password
            User userPassword=userDao.findUserByPassword(user.getPassword(),user.getEmail());
            // nếu có user ứng với password và email đó
            if(userPassword!=null){
                User findUser = userDao.Login(user.getEmail(), user.getPassword());
                if (findUser != null) {
                    if (findUser.getId() != null) {
                        // trả về thông tin user
                        return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(), "ok", findUser), HttpStatus.OK);
                    }
                }
            }
            // trả về user nhập sai pass
            return new ResponseEntity<>(new ResponseObject(HttpStatus.BAD_REQUEST.value(),"Password is wrong",null),HttpStatus.BAD_REQUEST);
        }
        // nếu ko tìm thấy user theo email- trả về user nhập sai email
        return new ResponseEntity<>(new ResponseObject(HttpStatus.BAD_REQUEST.value(),"Email is wrong",null),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ResponseObject> register(User user){
        User existing=userDao.findUserByEmail(user.getEmail());
        if(existing==null) {
            int OtpCode = emailSender.sendOtpCode(user.getEmail());
            User Newuser = new User();
            Newuser.setUserName(user.getUserName());
            Newuser.setEmail(user.getEmail());
            Newuser.setPassword(user.getPassword());
            Newuser.setIsGuest(false);
            Newuser.setStatus("pending");
            OTP otp = new OTP();
            otp.setOtpUser(Newuser);
            otp.setOtpCode(OtpCode);
            userDao.save(Newuser);
                otpDao.save(otp);
                return new ResponseEntity<>(new ResponseObject(HttpStatus.CREATED.value(), "created", Newuser), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponseObject(HttpStatus.CONFLICT.value(), "This email is already signup",null),HttpStatus.CONFLICT);
    }


    public ResponseEntity<ResponseObject> verifyAccount(User user,Integer otp){
        Optional<User> verifyUser=userDao.findById(user.getId());
        User NewUser=verifyUser.orElse(null);
        OTP thisUserOtp= otpDao.getOtpByUserId(user.getId());
        if(otp.equals(thisUserOtp.getOtpCode())) {
            NewUser.setStatus("approved");
            userDao.save(NewUser);
            otpDao.deleteById(thisUserOtp.getId());
            return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(), "OK", NewUser), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ResponseObject(HttpStatus.BAD_REQUEST.value(), "OTP code is wrong", null), HttpStatus.BAD_REQUEST);
        }
    }
}
