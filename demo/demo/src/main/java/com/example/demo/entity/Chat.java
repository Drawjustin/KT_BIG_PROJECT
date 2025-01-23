package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
//@SQLDelete(sql = "UPDATE chat SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Table(name = "chat")
public class Chat extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_seq")
    private Long chatSeq;

    // User와의 관계 (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    // ChatBox와의 관계 (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_box_seq", nullable = false)
    private ChatBox chatBox;

    @Column(name = "chat_filepath", length = 256)
    private String chatFilepath;

    @Column(name = "chat_question", length = 4096)
    private String chatQuestion;

    @Column(name = "chat_content", columnDefinition = "TEXT")
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