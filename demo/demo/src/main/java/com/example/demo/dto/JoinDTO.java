package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinDTO {
    private String userEmail;   // 사용자 이메일
    private String userId;      // 사용자 ID
    private String userPassword; // 사용자 비밀번호
    private String userName;    // 사용자 이름
    private String userNumber;  // 사용자 번호
    private String userRole;        // 사용자 역할부여

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserName(String userName) {this.userName = userName;}

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public void setUserRole(String userRole) {this.userRole = userRole;}
}
