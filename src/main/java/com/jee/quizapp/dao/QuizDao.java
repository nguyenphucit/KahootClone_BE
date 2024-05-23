package com.jee.quizapp.dao;

import com.jee.quizapp.model.Question;
import com.jee.quizapp.model.Quiz;
import com.jee.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizDao  extends JpaRepository<Quiz,Integer> {
    @Query("select q from Quiz q where q.quizhost=:user")
    List<Quiz> getQuizByUserId(User user);
}
