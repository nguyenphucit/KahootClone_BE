package com.jee.quizapp.services;

import com.jee.quizapp.dao.OTPDao;
import com.jee.quizapp.dao.UserDao;
import com.jee.quizapp.model.OTP;
import com.jee.quizapp.model.ResponseObject;
import com.jee.quizapp.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    public ResponseEntity<ResponseObject> createUser(User user){
        try{
           User newuser= userDao.save(user);
            return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(), "ok",newuser),HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ResponseObject> findUserById(Integer id){
        Optional<User> Optionaluser=userDao.findById(id);
        User user=Optionaluser.orElse(null);
        return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(),"ok",user),HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> deleteUnVerifyAccount(User user){
        userDao.deleteUnVerifyAccount(user.getId());
        return new ResponseEntity<>(new ResponseObject(HttpStatus.NO_CONTENT.value(),"deleted",null),HttpStatus.NO_CONTENT);
    }
}
