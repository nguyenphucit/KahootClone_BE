package com.jee.quizapp.services;

import com.jee.quizapp.dao.GameUserDao;
import com.jee.quizapp.dao.QuestionDao;
import com.jee.quizapp.dao.QuizDao;
import com.jee.quizapp.dao.UserDao;
import com.jee.quizapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizServices {
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;
    @Autowired
    GameUserDao gameUserDao;
    @Autowired
    UserDao userDao;

    //----------------------GET METHOD----------------------------//
    public ResponseEntity<ResponseObject> getQuizQuestion(Integer id){
        Optional<Quiz> quiz=quizDao.findById(id);
        if(!quiz.isEmpty()) {
            List<Question> questionFromDB = quiz.get().getQuestions();
            List<Question> questionForUser = new ArrayList<>();
            for (Question q : questionFromDB) {
                questionForUser.add(q);
            }
            return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(), "ok", questionForUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseObject(HttpStatus.NOT_FOUND.value(), "this quiz is not exist", null), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseObject> getQuizByUserId(Integer userId){
        Optional<User> userOptional = userDao.findById(userId);
        if(!userOptional.isEmpty()) {
            User user = userOptional.orElse(null);
            List<Quiz> quizList = quizDao.getQuizByUserId(user);
            return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(), "ok", quizList), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ResponseObject(HttpStatus.NOT_FOUND.value(), "cant find quiz belong to this userId", null), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ResponseObject> getAll(){
        List<Quiz> quizList=quizDao.findAll();
        return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(),"ok",quizList),HttpStatus.OK);
    }

    //------------------POST METHOD-------------------------------//
    public ResponseEntity<ResponseObject> createQuiz(String title, String category, Integer numQ){
        Quiz quiz=new Quiz();
        quiz.setTitle(title);
        List<Question> questionList=questionDao.findRandomQuestionByCategory(category,numQ);
        quiz.setQuestions(questionList);
        System.out.println(quiz);
        quizDao.save(quiz);
        return new ResponseEntity<>(new ResponseObject(HttpStatus.CREATED.value(),"successfully create quiz",quiz), HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseObject> createQuizAdmin(List<Question> listQuestion,Integer userId,String quizTitle){
        List<Question> quizQuestions = new ArrayList<>();
        for (Question question : listQuestion) {
            Question tempQuestion = questionDao.save(question);
            quizQuestions.add(tempQuestion);
        }
        Quiz quiz = new Quiz();
        Optional<User> userOptional = userDao.findById(userId);
        User user = userOptional.orElse(null);
        quiz.setQuizhost(user);
        quiz.setQuestions(quizQuestions);
        quiz.setTitle(quizTitle);
        Quiz responseQuiz = quizDao.save(quiz);
        return new ResponseEntity<>(new ResponseObject(HttpStatus.CREATED.value(), "created", responseQuiz), HttpStatus.CREATED);
    }



    public ResponseEntity<ResponseObject> submit(List<Response> response,Integer quizId,Integer userId,String gameId){
        Integer score=0;
        Optional<Quiz> quiz=quizDao.findById(quizId);
        List<Question> questionList=quiz.get().getQuestions();
        // calculate score for this user
        for(Response rp : response){
            for(Question question: questionList){
                if(rp.getId().equals(question.getId())){
                    if(rp.getAnswer().equalsIgnoreCase(question.getAnswer()))
                        score++;
                }
            }
        }
        GameUser gameUser=gameUserDao.findUserSubmit(userId,gameId);
        gameUser.setScore(score);
        gameUserDao.save(gameUser);
        return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(),"ok",gameUser),HttpStatus.OK);
    }


    //---------------PUT METHOD--------------------------------//
    public ResponseEntity<ResponseObject> updateQuiz(List<Question> listQuestion,Integer quizId,String quizTitle){
        Optional<Quiz> quizOptional=quizDao.findById(quizId);
        Quiz quiz=quizOptional.orElse(null);
        if(quiz!=null) {
            List<Question> quizQuestions = new ArrayList<>();
            for (Question question : listQuestion) {
                Question tempQuestion = questionDao.save(question);
                quizQuestions.add(tempQuestion);
            }
            quiz.setTitle(quizTitle);
            quiz.setQuestions(quizQuestions);
            return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(), "updated", quizDao.save(quiz)), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ResponseObject(HttpStatus.NOT_FOUND.value(), "not found quiz", null), HttpStatus.NOT_FOUND);
        }
    }

    //-------------------DELETE METHOD--------------------------//
    public ResponseEntity<ResponseObject> deleteQuiz(Integer quizId) {
        try {
            Optional<Quiz> optionalQuiz = quizDao.findById(quizId);
            if (optionalQuiz.isPresent()) {
                quizDao.deleteById(quizId);
                return new ResponseEntity<>(new ResponseObject(HttpStatus.OK.value(), "Quiz deleted successfully", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(HttpStatus.NOT_FOUND.value(), "Quiz not found", null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
