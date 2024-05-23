package com.jee.quizapp.dao;

import com.jee.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User,Integer> {
    User findByUserName(String userName);
    @Query(value="SELECT us from User us where us.password=:passWord and us.email=:email and us.status <> 'pending' ")
    User Login(String email,String passWord);
    @Query(value="SELECT us.id from User us where us.isGuest=true and us.id=:userid")
    Integer findGuest(Integer userid);
    @Query(value="DELETE FROM User us where us.status='pending' and us.id=:id")
    Void deleteUnVerifyAccount(Integer id);
    User findUserByEmail(String email);
    @Query(value="SELECT us FROM User us WHERE us.email=:email and us.password=:password")
    User findUserByPassword(String password,String email);
}
