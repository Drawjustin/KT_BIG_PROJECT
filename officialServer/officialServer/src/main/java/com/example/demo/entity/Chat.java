package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "chat")
public class Chat extends baseEntity {

    @Id
    @GeneratedValue
    private Long chatSeq;

    private Long userSeq;
    private Long chatBoxSeq;
    private String chatFilepath;
    private String chatQuestion;
    @Lob
    private String chatContent;
}
