package com.jee.quizapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    Integer otpCode;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User otpUser;
}
