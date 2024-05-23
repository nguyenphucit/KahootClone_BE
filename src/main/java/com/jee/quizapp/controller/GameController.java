package com.jee.quizapp.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jee.quizapp.dao.UserDao;
import com.jee.quizapp.model.Game;
import com.jee.quizapp.model.ResponseObject;
import com.jee.quizapp.model.User;
import com.jee.quizapp.services.GameServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("game")
public class GameController {
    @Autowired
    GameServices gameServices;
    @Autowired
    UserDao userDao;

    ///////////////////////////HTTP PROTOCOL////////////////////////////////////////
    //-----------------------------GET METHOD-----------------------//
    //Get game by id ( get quizList)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getGameById(@PathVariable String id){
        return gameServices.getGameById(id);
    }

    // Get user list in game by game Id
    @GetMapping("/{id}/user")
    public ResponseEntity<ResponseObject> getUsersInGame(@PathVariable String  id){
        return gameServices.getUserInGame(id);
    }

    //-----------------------------POST METHOD-----------------------//
    // Create game
    @PostMapping("create")
    public ResponseEntity<ResponseObject> createGame(@RequestBody Map<String, Object> requestBody){
        List<User> users = (List<User>) requestBody.get("user");
        Integer quizId = (Integer) requestBody.get("quizId");
        Integer userId = (Integer) requestBody.get("hostId");
        return gameServices.createGame(users,quizId,userId);
    }

    //Check host ( find user by id and see that's host or not)
    @PostMapping("checkhost/{id}")
    public ResponseEntity<ResponseObject> checkHost(@PathVariable String id){
        return gameServices.checkHost(id);
    }

    //Check exist ( find game by id - check if game code in database or not)
    @PostMapping("checkexist/{id}")
    public ResponseEntity<ResponseObject> checkIfGameExist(@PathVariable String id)
    {
        return gameServices.checkGame(id);
    }


    ////////////////////////////SOCKET PROTOCOL////////////////////////////////
    //Add user to lobby ( to game ) and let other user see it
    @MessageMapping("/add-user")
    @SendTo("/topic/lobby-user")
    public ResponseEntity<ResponseObject> addUserToGame(@Payload String gameAnduserId){
        Gson gson=new Gson();
        Map<String, Object> payload = gson.fromJson(gameAnduserId, new TypeToken<Map<String, Object>>() {}.getType());
        User user = gson.fromJson(gson.toJson(payload.get("user")), User.class);
        Game game = gson.fromJson(gson.toJson(payload.get("game")), Game.class);
        String gameId=game.getId();
        Integer userId=user.getId();
        return gameServices.addUserToGame(gameId,userId);
    }

    //Start game and every user join that game see it the same time
    @MessageMapping("/start")
    @SendTo("/topic/game")
    public ResponseEntity<ResponseObject> StartGame(){
        return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(),"ok",null ),HttpStatus.OK);
    }

    //Move to next question and every user join that game see it the same time
    @MessageMapping("/nextQuestion")
    @SendTo("/topic/game")
    public ResponseEntity<ResponseObject> NextQuestion(){
        return new ResponseEntity<>(new ResponseObject(HttpStatus.CREATED.value(),"ok",null ),HttpStatus.CREATED);
    }

    //End game, return leaderBoard and every user join that game see it the same time
    @MessageMapping("/endGame")
    @SendTo("/topic/game")
    public ResponseEntity<ResponseObject> endGame(@Payload  String gameId){
        return gameServices.endGame(gameId);
    }

    //Delete game which is already done
    @MessageMapping("/deleteGame")
    @SendTo("/topic/game")
    public ResponseEntity<ResponseObject> deleteGame(@Payload  String gameId){
        return gameServices.deleteGame(gameId);
    }

}
