package com.example.demo.dto;

import com.example.demo.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class JoinDTO {
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수 입력값입니다")
    private String userEmail;
    private String userId;
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자리여야 합니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다")
    private String userPassword;
    private String userName;
    private String userNumber;
    private String userRole;

    // Entity 변환 메서드
    public UserEntity toEntity(PasswordEncoder passwordEncoder) {
        return new UserEntity(
                this.userEmail,
                this.userId,
                passwordEncoder.encode(this.userPassword),
                this.userName,
                this.userNumber,
                this.userRole
        );
    }

    // setter는 필요한 경우에만 유지
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}