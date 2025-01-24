package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCodeCustom implements ErrorCode {

    NO_AUTHORIZATION(HttpStatus.FORBIDDEN, "해당 기능에 대한 권한이 없는 사용자입니다"),
    DUPLICATE_VALUE(HttpStatus.BAD_REQUEST, "중복 값이 허용되지 않습니다"),
    NO_MEMBER(HttpStatus.BAD_REQUEST, "해당하는 사용자가 존재하지 않습니다"),
    NO_DEPARTMENT(HttpStatus.BAD_REQUEST,"해당하는 부서가 존재하지 않습니다."),
    NO_COMPLAINT(HttpStatus.BAD_REQUEST,"해당하는 민원이 존재하지 않습니다."),
    REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "필수 입력 값이 누락되었습니다."),
    FAIL_SAVED_FILE(HttpStatus.BAD_REQUEST,"알 수 없는 이유로 파일을 저장하는데 실패했습니다."),
    HEADER_ACCESS_TOKEN_NOT_EXISTS(HttpStatus.BAD_REQUEST, "access token이 존재하지 않습니다"),
    COOKIE_REFRESH_TOKEN_NOT_EXISTS(HttpStatus.BAD_REQUEST, "refresh token이 존재하지 않습니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "사용가능한 refresh token이 아닙니다"),
    WRONG_ACCESS_WITHOUT_AUTHORIZATION(HttpStatus.FORBIDDEN, "비정상적인 접근입니다"),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 잘못되었습니다"),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다."),
    ENCRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "token 암호화가 실패하였습니다"),
    DECRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "token 복호화가 실패하였습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
