package com.example.demo.controller;

import com.example.demo.repository.RefreshRepository;
import com.example.demo.service.ReissueService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//쿠키: 만약에 access토큰 만료시 다시 만들어줌
@Controller
@ResponseBody
@RequestMapping("/api")
public class ReissueController {

    private final ReissueService reissueService;


    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            return reissueService.reissue(request, response);
        } catch (ExpiredJwtException e) {
            // 토큰 만료 관련 로깅
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body("토큰이 만료되었습니다. 다시 로그인해주세요.");
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .body("토큰 재발급 중 오류가 발생했습니다.");
        }
    }

}