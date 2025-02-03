package com.example.demo.controller;

import com.example.demo.dto.request.JoinRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.LogoutRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.CommonResponse;
import com.example.demo.dto.response.ErrorResponse;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.InvalidTokenException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.AuthService;
import com.example.demo.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/join")
    public ResponseEntity<CommonResponse<String>> join(@RequestBody @Valid JoinRequest request) {
        try {
            authService.join(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(CommonResponse.success("회원가입 성공", request.userEmail()));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(CommonResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("회원가입 실패", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.error("회원가입 처리 중 오류가 발생했습니다"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<AuthResponse>> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.login(request);

            // Refresh Token을 HttpOnly 쿠키에 설정
            Cookie refreshTokenCookie = cookieUtil.createCookie(authResponse.refreshToken());
            response.addCookie(refreshTokenCookie);

            // Access Token만 응답 본문에 포함
            return ResponseEntity.ok(CommonResponse.success("로그인 성공",
                    new AuthResponse(
                            authResponse.userEmail(),
                            authResponse.accessToken(),
                            null
                    )));

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(CommonResponse.error("이메일 또는 비밀번호가 잘못되었습니다"));
        } catch (Exception e) {
            log.error("로그인 실패", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.error("로그인 처리 중 오류가 발생했습니다"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(
            @RequestBody @Valid LogoutRequest request,
            HttpServletResponse response) {
        try {
            authService.logout(request.userEmail());

            // Refresh Token 쿠키 만료
            Cookie cookie = cookieUtil.createCookie("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            return ResponseEntity.ok(CommonResponse.success("로그아웃 성공", null));
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(CommonResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("로그아웃 실패", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.error("로그아웃 처리 중 오류가 발생했습니다"));
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<TokenResponse>> reissue(
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String refreshToken = cookieUtil.extractRefreshToken(request);
            if (refreshToken == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(CommonResponse.error("Refresh Token이 없습니다"));
            }

            // refresh 토큰이 유효하므로 새로운 토큰 발급
            TokenResponse tokenResponse = authService.reissueToken(refreshToken);

            // 새로운 Refresh Token을 쿠키에 설정
            Cookie newRefreshTokenCookie = cookieUtil.createCookie(tokenResponse.refreshToken());
            response.addCookie(newRefreshTokenCookie);

            return ResponseEntity.ok(CommonResponse.success("토큰 재발급 성공",
                    new TokenResponse(
                            tokenResponse.accessToken(),
                            null  // refreshToken은 쿠키로 전송되므로 응답에서 제외
                    )));
        } catch (InvalidTokenException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(CommonResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("토큰 재발급 실패", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.error("토큰 재발급 중 오류가 발생했습니다"));
        }
    }

    // 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = ErrorResponse.of("유효성 검사 오류: ", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}