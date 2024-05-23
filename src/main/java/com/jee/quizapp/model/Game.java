package com.jee.quizapp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@Entity
public class Game {
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", type = com.jee.quizapp.helper.GameIdGenerator.class)
    private String id;
    @ManyToOne
    private Quiz quiz;
    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    @OneToMany(mappedBy = "game",cascade = CascadeType.ALL)
    @Transient
    private List<GameUser> gameUsers;
}
