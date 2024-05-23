package com.jee.quizapp.controller;

import com.jee.quizapp.model.ResponseObject;
import com.jee.quizapp.model.User;
import com.jee.quizapp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("auth")
public class AuthController {
    @Autowired
    AuthService authService;
    //---------------------POST METHOD----------------------------//
    // Login function
    @PostMapping("/login")
    public ResponseEntity<ResponseObject> Login(@RequestBody User user){
        return authService.login(user);
    }

    // Register function ( create account with status " pending", waiting for otp verify)
    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register (@RequestBody User user){
        return authService.register(user);
    }

    // Verify Otp by userId
    @PostMapping("/verifyOtp")
    public ResponseEntity<ResponseObject> verifyOTP(@RequestBody User user, @RequestParam Integer otp){
        return authService.verifyAccount(user,otp);
    }
}
