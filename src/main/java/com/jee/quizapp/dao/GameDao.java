package com.jee.quizapp.dao;

import com.jee.quizapp.model.Game;
import com.jee.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameDao extends JpaRepository<Game,Integer> {
    @Query(nativeQuery = true,value="SELECT * FROM game g where g.id=:id")
    public Optional<Game> findByGameId(String id);
}
