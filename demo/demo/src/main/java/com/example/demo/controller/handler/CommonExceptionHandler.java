//package com.example.demo.controller.handler;
//
//import com.example.demo.exception.RestApiException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class CommonExceptionHandler {
//
//    @ExceptionHandler(RestApiException.class)
//    public ResponseEntity<String> handleRestApiException(RestApiException ex) {
//        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(ex.getMessage());
//    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGeneralException(Exception ex) {
//        // 모든 기타 예외에 대한 처리
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("서버 내부 오류가 발생했습니다. 나중에 다시 시도해주세요.");
//    }
//}
