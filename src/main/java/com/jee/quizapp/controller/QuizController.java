package com.jee.quizapp.controller;

import com.jee.quizapp.model.Question;
import com.jee.quizapp.model.Quiz;
import com.jee.quizapp.model.ResponseObject;
import com.jee.quizapp.services.QuizServices;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jee.quizapp.model.Response;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("quiz")
public class QuizController {
    @Autowired
    QuizServices quizServices;

    //---------------------GET METHOD----------------------------//
    // get Quiz by id ( get all question in that quiz and quizCreator(user)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getQuiz(@PathVariable Integer id){
        return quizServices.getQuizQuestion(id);
    }

    // get All quiz in database
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllQuiz(@RequestParam(required = false) Integer userId){
        if(userId!=null)
            return quizServices.getQuizByUserId(userId);
        else
            return quizServices.getAll();
    }

    //---------------------POST METHOD----------------------------//
    //create new quiz random by 3 params which are category,numQ (number of question),title ( title of this new quiz)--not use in project
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createQuiz(@RequestParam(required = false) String category, @RequestParam Integer numQ, @RequestBody String title){
        return quizServices.createQuiz(title,category,numQ);
    }

    //create quiz by 3 params userId( quizCreator), listQuestion ( list question for this quiz) and title ( title for this new quiz)
    @PostMapping("/createQuiz/{userId}")
    public ResponseEntity<ResponseObject> createQuizByAdmin(@RequestBody List<Question> listQuestion,@PathVariable Integer userId,@RequestParam String quizTitle){
        return quizServices.createQuizAdmin(listQuestion,userId,quizTitle);
    }

    //submit answer by 3 params userId (user who submit) listResponse( {id(questionId),answer(userAnswer)}, quizId ( to find out which responseList should be submit to),gameId to update score for submit user
    @PostMapping("submit/{id}")
    public ResponseEntity<ResponseObject> submit(@RequestBody List<Response> response, @PathVariable Integer id,@RequestParam Integer userId,@RequestParam String gameId){
        return quizServices.submit(response,id,userId,gameId);
    }

    //---------------------PUT METHOD----------------------------//
    //update question list,title for quiz by id
    @PutMapping("/updateQuiz/{quizId}")
    public ResponseEntity<ResponseObject> updateQuizByAdmin(@RequestBody List<Question> listQuestion,@PathVariable Integer quizId,@RequestParam String quizTitle){
        return quizServices.updateQuiz(listQuestion,quizId,quizTitle);
    }

    //---------------------DELETE METHOD----------------------------//
    //delete quiz by id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteQuiz(@PathVariable Integer id){
        return quizServices.deleteQuiz(id);
    }

}
