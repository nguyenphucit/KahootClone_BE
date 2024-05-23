package com.jee.quizapp.services;

import com.jee.quizapp.dao.QuestionDao;
import com.jee.quizapp.model.Question;
import com.jee.quizapp.model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class QuestionServices {
    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<List<Question>> getAll(){
        try{
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category){
        try{
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<String> createQuestion(Question question){
        try{
            questionDao.save(question);
            return new ResponseEntity<>("successfully create new question", HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("can't create new question",HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<String> deleteQuestionById(Integer id) {
        try{
            questionDao.deleteById(id);
            return new ResponseEntity<>("successfully remove question from database", HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("can't remove question",HttpStatus.BAD_GATEWAY);
    }
}
