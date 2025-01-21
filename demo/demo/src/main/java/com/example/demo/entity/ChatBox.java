package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_box")
public class ChatBox extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_box_seq")
    private Long chatBoxSeq;

    @Column(name = "chat_box_title", length = 100)
    private String chatBoxTitle;

    @Builder
    public ChatBox(String chatBoxTitle) {
        this.chatBoxTitle = chatBoxTitle;
    }
}