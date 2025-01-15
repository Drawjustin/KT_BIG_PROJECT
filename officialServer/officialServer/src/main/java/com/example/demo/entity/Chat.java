package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat")
public class Chat extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatSeq;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatBoxSeq")
    private ChatBox chatBox;
    private String chatFilepath;
    private String chatQuestion;
    @Lob
    private String chatContent;

    @Builder
    public Chat(User user, ChatBox chatBox, String chatFilepath, String chatQuestion, String chatContent) {
        this.user = user;
        this.chatBox = chatBox;
        this.chatFilepath = chatFilepath;
        this.chatQuestion = chatQuestion;
        this.chatContent = chatContent;
    }
}
