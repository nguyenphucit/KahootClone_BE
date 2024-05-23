package com.jee.quizapp.controller;

import com.jee.quizapp.model.Question;
import com.jee.quizapp.services.QuestionServices;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("question")
public class QuestionController {

    @Autowired
    QuestionServices questionServices;
    //---------------------GET METHOD----------------------------//
    @GetMapping("")
    public ResponseEntity<List<Question>> getAllQuestion(){
       return questionServices.getAll();
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Question>> getAllQuestionByCategory(@PathVariable String category){
        return questionServices.getQuestionByCategory(category);
    }

    //---------------------POST METHOD----------------------------//
    @PostMapping("/create")
    public ResponseEntity<String>createQuestion(@RequestBody  Question question){
        return questionServices.createQuestion(question);
    }

    //---------------------DELETE METHOD----------------------------//
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestionById(@PathVariable Integer id){
        return questionServices.deleteQuestionById(id);
    }
}
