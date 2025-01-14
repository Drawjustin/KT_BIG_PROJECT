package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_box")
public class ChatBox extends baseEntity{

    @Id
    @GeneratedValue
    private Long chatBoxSeq;

    private String chatBoxTitle;

}
