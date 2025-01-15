package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_box")
public class ChatBox extends baseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatBoxSeq;

    private String chatBoxTitle;

    @Builder
    public ChatBox(String chatBoxTitle) {
        this.chatBoxTitle = chatBoxTitle;
    }
}
