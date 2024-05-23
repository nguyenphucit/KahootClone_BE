package com.jee.quizapp.dao;

import com.jee.quizapp.model.GameUser;
import com.jee.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameUserDao extends JpaRepository<GameUser,Integer> {
    @Query(value ="SELECT users_id FROM game_users gu WHERE game_id= :id" ,nativeQuery = true)
    List<Integer> findAllUserInGame(String id);

    @Query(value ="SELECT * FROM game_users gu WHERE game_id= :gameId and users_id= :userId" ,nativeQuery = true)
    GameUser findUserSubmit(Integer userId,String gameId);

    @Query("SELECT gu.user, gu.score FROM GameUser gu WHERE gu.game.id = :gameId ORDER BY gu.score DESC")
    List<Object[]> findUserScoreByGameId(String gameId);

    @Query(value = "DELETE  FROM  game_users gu WHERE game_id=:gameId AND users_id=:userId",nativeQuery = true)
    void deleteGameUser(String gameId,Integer userId);

    @Query(value="SELECT users_id FROM game_users where  and game_id=:id",nativeQuery = true)
    List<Integer> findAllGuestInGame(String id);
}
