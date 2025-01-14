package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin")
public class Admin extends baseEntity{
    @Id
    @GeneratedValue
    private Long userSeq;
    private Integer userId;
    private String userPassword;
    private String userName;
    private String userNumber;
    private String userEmail;

}
