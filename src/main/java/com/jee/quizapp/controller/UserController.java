package com.jee.quizapp.controller;

import com.google.gson.reflect.TypeToken;
import com.jee.quizapp.model.Email;
import com.jee.quizapp.model.Game;
import com.jee.quizapp.model.ResponseObject;
import com.jee.quizapp.model.User;
import com.jee.quizapp.services.EmailSender;
import com.jee.quizapp.services.GameServices;
import com.jee.quizapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    GameServices gameServices;

    ////////////////////////HTTP PROTOCOL/////////////////////////////////////
    // Create new user
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    // Get User by id
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findUserById(@PathVariable Integer id){
        return userService.findUserById(id);
    }

    /////////////////////////SOCKET PROTOCOL/////////////////////////////////
    //Add guest ( user haven't register ) to game - ( create Game_users ) and every user see it the same time
    @MessageMapping("/user")
    @SendTo("/topic/users")
    public Object sendUserInfo(@Payload  String jsonPayload){
        Gson gson=new Gson();
        Map<String, Object> payload = gson.fromJson(jsonPayload, new TypeToken<Map<String, Object>>() {}.getType());
        User user = gson.fromJson(gson.toJson(payload.get("user")), User.class);
        Game game = gson.fromJson(gson.toJson(payload.get("game")), Game.class);

        String userName=user.getUserName();
        String gameId=game.getId();
        return gameServices.addGuestToGame(gameId,userName);
    }

}
