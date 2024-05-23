package com.jee.quizapp.dao;

import com.jee.quizapp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question,Integer> {
    List<Question> findByCategory(String category);

    @Query(value ="SELECT * FROM question q WHERE(:category is null or q.category=:category) ORDER BY RAND() LIMIT :numQ " ,nativeQuery = true)
    List<Question> findRandomQuestionByCategory(String category,Integer numQ);

    @Query(value ="SELECT answer FROM question q WHERE q.id = :id " ,nativeQuery = true)
    String findCorrectAnswerById(Integer id);
}
