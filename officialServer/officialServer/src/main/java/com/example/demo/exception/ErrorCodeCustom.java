package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCodeCustom implements ErrorCode {
    NO_MEMBER(HttpStatus.BAD_REQUEST, "해당하는 사용자가 존재하지 않습니다"),
    NO_DEPARTMENT(HttpStatus.BAD_REQUEST,"해당하는 부서가 존재하지 않습니다."),
    NO_COMPLAINT(HttpStatus.BAD_REQUEST,"해당하는 민원이 존재하지 않습니다."),
    NO_COMPLAINT_COMMENT(HttpStatus.BAD_REQUEST,"해당하는 민원 답변이 존재하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
