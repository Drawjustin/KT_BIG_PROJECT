package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member extends baseEntity {

    @Id
    @GeneratedValue
    private Long memberSeq;

    private Integer memberId;
    private String memberPassword;
    private String memberName;
    private String memberEmail;
}
