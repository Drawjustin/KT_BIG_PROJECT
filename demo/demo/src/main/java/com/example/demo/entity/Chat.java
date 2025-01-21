package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat")
public class Chat {

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
    private String chatContent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted",nullable = false)
    private Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public Chat(User user, ChatBox chatBox, String chatFilepath, String chatQuestion, String chatContent) {
        this.user = user;
        this.chatBox = chatBox;
        this.chatFilepath = chatFilepath;
        this.chatQuestion = chatQuestion;
        this.chatContent = chatContent;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }
}