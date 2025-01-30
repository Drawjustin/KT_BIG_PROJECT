package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
//@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_seq", nullable = false)
    private Team team;

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

    @Column(name = "user_role", length = 50)
    private String userRole;

    @Builder
    public User(Team team, String userId, String userPassword, String userName, String userNumber, String userEmail, String userRole) {
        this.team = team;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNumber = userNumber;
        this.userEmail = userEmail;
        this.userRole = userRole;
    }
}
