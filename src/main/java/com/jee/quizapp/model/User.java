package com.jee.quizapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userName;
    private String password="";
    private Boolean isGuest=true;
    private String email;
    private String status;

    @Transient
    @OneToMany(mappedBy = "otpUser", cascade = CascadeType.ALL)
    private List<OTP> userOtps = new ArrayList<>();

    @Transient
    @OneToMany(mappedBy = "quizhost", cascade = CascadeType.ALL)
    private List<Quiz> hostedQuizzes = new ArrayList<>();

    @Transient
    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL)
    private List<Game> hostedGame = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @Transient
    private List<GameUser> userGames;
}
