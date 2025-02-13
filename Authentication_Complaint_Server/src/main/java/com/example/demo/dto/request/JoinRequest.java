package com.example.demo.dto.request;

import com.example.demo.entity.Member;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Builder
public record JoinRequest(
        @Email(message = "올바른 이메일 형식이 아닙니다")
        String memberEmail,

        @Size(min = 8, max = 20, message = "아이디는 8~20자리여야 합니다")
        @NotBlank(message = "아이디는 필수 입력값입니다")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "아이디는 영문, 숫자를 포함해야 합니다")
        String memberId,

        @Size(min = 8, max = 20, message = "비밀번호는 8~20자리여야 합니다")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다")
        String memberPassword,

        @NotBlank(message = "이름은 필수 입력값입니다")
        @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 한글 2~5글자여야 합니다")
        String memberName,

        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다")
        String memberNumber

) {
    public Member toEntity(PasswordEncoder passwordEncoder) {
        Member member = new Member(
                this.memberEmail,
                this.memberId,
                passwordEncoder.encode(this.memberPassword),
                this.memberName,
                this.memberNumber,
                "USER"
        );
        return member;
    }
}