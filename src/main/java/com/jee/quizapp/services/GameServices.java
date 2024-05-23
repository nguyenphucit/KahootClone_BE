package com.jee.quizapp.services;

import com.jee.quizapp.dao.GameDao;
import com.jee.quizapp.dao.GameUserDao;
import com.jee.quizapp.dao.QuizDao;
import com.jee.quizapp.dao.UserDao;
import com.jee.quizapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameServices{
    @Autowired
    GameDao gameDao;
    @Autowired
    QuizDao quizDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GameUserDao gameUserDao;
    //----------------GET METHOD--------------------------------//
    public ResponseEntity<ResponseObject> getGameById(String gameId){
        Optional<Game> game=gameDao.findByGameId(gameId);
        Game existGame=game.orElse(null);
        if(existGame!=null)
        return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(),"ok",existGame),HttpStatus.OK);
        return new ResponseEntity<>(new ResponseObject(HttpStatus.NOT_FOUND.value(),"not found game with this id",null),HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseObject> getUserInGame(String gameId){
        List<Integer> userIds =gameUserDao.findAllUserInGame(gameId);
        List<User> users=new ArrayList<>();
        for(Integer id : userIds){
            Optional<User> userOptional=userDao.findById(id);
            User user = userOptional.orElse(null);
            users.add(user);
        }
        return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(),"successfully get data",users),HttpStatus.OK);
    }

    //----------------POST METHOD--------------------------------//
    public ResponseEntity<ResponseObject> createGame(List<User> user, Integer id,Integer hostId){
        Optional<Quiz> quizOptional = quizDao.findById(id);
        Quiz quiz = quizOptional.orElse(null);
        Optional<User> userOptional=userDao.findById(hostId);
        User host=userOptional.orElse(null);
        Game game=new Game();
        game.setQuiz(quiz);
        game.setHost(host);
        gameDao.save(game);
        return new ResponseEntity<>(new ResponseObject(HttpStatus.CREATED.value(), "successfully create game",game),HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseObject> addUserToGame(String GameId, Integer userId){
        Optional<User> userOptional=userDao.findById(userId);
        User user = userOptional.orElse(null);
        Optional<Game> gameOptional=gameDao.findByGameId(GameId);
        Game game= gameOptional.orElse(null);
        GameUser gameUser =new GameUser();
        gameUser.setGame(game);
        gameUser.setUser(user);
        gameUser.setScore(0);
        gameUserDao.save(gameUser);
        return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(),"successfully add user",user),HttpStatus.OK);
    }
    public ResponseEntity<String> addGuestToGame(String GameId,String GuestName){
        Optional<Game> gameOptional=gameDao.findByGameId(GameId);
        Game game= gameOptional.orElse(null);
        GameUser gameUser =new GameUser();
        if(game!=null){
            User guest=new User();
            guest.setUserName(GuestName);
            userDao.save(guest);
            gameUser.setUser(guest);
            gameUser.setGame(game);
            gameUserDao.save(gameUser);
            return new ResponseEntity<>("Successfully join game",HttpStatus.OK);
        }
        return new ResponseEntity<>("game id is wrong",HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<ResponseObject> checkGame(String gameId){
        Optional<Game> game=gameDao.findByGameId(gameId);
        Game existGame=game.orElse(null);
        if(!existGame.getId().isBlank()){
            return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(),"ok",null),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ResponseObject(HttpStatus.NOT_FOUND.value(),"this game isn't exist",null),HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseObject> checkHost(String gameId){
        Optional<Game> game=gameDao.findByGameId(gameId);
        Game existGame=game.orElse(null);
        if(existGame!=null) {
            User host = existGame.getHost();
            return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(), "ok", host), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseObject(HttpStatus.NOT_FOUND.value(), "cant find game with this id",null), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseObject> endGame(String gameId){
       List<Object[]> gameUserList= gameUserDao.findUserScoreByGameId(gameId);
       return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(),"ok",gameUserList),HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> deleteGame(String gameId){
        List<Integer> userIds=gameUserDao.findAllUserInGame(gameId);
        for(Integer id: userIds){
            Integer guestId=userDao.findGuest(id);
                if(guestId!=null)
              userDao.deleteById(guestId);
        }
        return new ResponseEntity<>(new ResponseObject(HttpStatus.NO_CONTENT.value(),"deleted",userIds),HttpStatus.NO_CONTENT);
    }
}
