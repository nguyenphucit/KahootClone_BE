package com.jee.quizapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "game_users")
public class GameUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    private Integer score;
}