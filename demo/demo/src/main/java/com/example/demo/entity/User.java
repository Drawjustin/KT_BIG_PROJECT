package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "user_id", length = 30, nullable = false)
    private String userId;

    @Column(name = "user_password", length = 512, nullable = false)
    private String userPassword;

    @Column(name = "user_name", length = 30)
    private String userName;

    @Column(name = "user_number", length = 13)
    private String userNumber;

    @Column(name = "user_email", length = 30)
    private String userEmail;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Complaint> complaints = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

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
    public User(String userId, String userPassword, String userName, String userNumber, String userEmail) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNumber = userNumber;
        this.userEmail = userEmail;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    // 추가된 생성자 Test용
    public User(Long userSeq) {
        this.userSeq = userSeq;
    }


    public void updateUser(String name, String number, String email) {
        this.userName = name;
        this.userNumber = number;
        this.userEmail = email;
        this.updatedAt = LocalDateTime.now();
    }
}
