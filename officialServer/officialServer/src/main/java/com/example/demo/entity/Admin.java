package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "admin")
public class Admin extends baseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;
    private Integer userId;
    private String userPassword;
    private String userName;
    private String userNumber;
    private String userEmail;

}
