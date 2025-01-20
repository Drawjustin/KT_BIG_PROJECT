package com.example.demo.service;

import com.example.demo.dto.JoinDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {
        // JoinDTO에서 사용자 정보를 가져오기
        String email = joinDTO.getUserEmail();
        String password = joinDTO.getUserPassword();
        String userId = joinDTO.getUserId();
        String userName = joinDTO.getUserName();
        String userNumber = joinDTO.getUserNumber();
        String userRole = joinDTO.getUserRole();

        // 이메일 중복 확인
        if (userRepository.existsByUserEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 새로운 회원 정보 생성
        UserEntity user = new UserEntity(
                email,
                userId,
                bCryptPasswordEncoder.encode(password), // 비밀번호 암호화
                userName,
                userNumber,
                "USER" // 역할 디폴트로 "USER" 설정
        );

        // 데이터베이스에 저장
        userRepository.save(user);
    }
}
