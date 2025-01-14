package com.example.demo.entity;


import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "user")
public class User extends baseEntity {
    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    private String userId;
    private String userPassword;
    private String userName;
    private String userNumber;
    private String userEmail;

    public User(String userId, String userPassword, String userName, String userNumber, String userEmail) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNumber = userNumber;
        this.userEmail = userEmail;
    }
}
