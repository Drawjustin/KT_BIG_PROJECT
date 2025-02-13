package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
//@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Table(name="user")
public class User extends baseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_seq", nullable = false)
    private Team team;

    @Column(name = "user_email", unique = true, nullable = false, length = 255)
    private String userEmail; // 사용자의 이메일

    @Column(name = "user_id", length = 30, nullable = false)
    private String userId;

    @Column(name = "user_password", nullable = false, length = 255)
    private String userPassword;

    @Column(name = "user_name", nullable = false, length = 30)
    private String userName;

    @Column(name = "user_number", length = 13)
    private String userNumber;

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
        this.userRole = (userRole != null) ? userRole : "USER";
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
