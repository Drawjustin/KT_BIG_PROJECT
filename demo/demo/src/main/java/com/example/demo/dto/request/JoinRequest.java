package com.example.demo.dto.request;

import com.example.demo.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public record JoinRequest(
        @Email(message = "올바른 이메일 형식이 아닙니다")
        String userEmail,

        @NotBlank(message = "아이디는 필수 입력값입니다")
        String userId,

        @Size(min = 8, max = 20, message = "비밀번호는 8~20자리여야 합니다")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다")
        String userPassword,

        @NotBlank(message = "이름은 필수 입력값입니다")
        @Pattern(regexp = "^[가-힣]{3}$", message = "이름은 한글 3글자여야 합니다")
        String userName,

        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다")
        String userNumber

) {
    public UserEntity toEntity(PasswordEncoder passwordEncoder) {
        return new UserEntity(
                this.userEmail,
                this.userId,
                passwordEncoder.encode(this.userPassword),
                this.userName,
                this.userNumber,
                "USER"
        );
    }
}


