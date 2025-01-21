package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

//토큰 저장 엔티티
@Entity
@Getter
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private String refresh;
    private String expiration;


    public void setUserSeq(Long id) {
        this.id = id;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

}
