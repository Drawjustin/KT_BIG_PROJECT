package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;

    public PasswordService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,JavaMailSender emailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender=emailSender;
    }

    public void sendTemporaryPassword(String email) {
        UserEntity user = userRepository.findByUserEmail(email);
        if (user == null) throw new RuntimeException("사용자를 찾을 수 없습니다.");

        String tempPassword = generateRandomPassword();
        user.setUserPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        sendEmail(email, "임시 비밀번호: " + tempPassword);
    }

    public void updatePassword(String email, String newPassword) {
        UserEntity user = userRepository.findByUserEmail(email);
        if (user == null) throw new RuntimeException("사용자를 찾을 수 없습니다.");

        user.setUserPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private void sendEmail(String to, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("임시 비밀번호 발급");
        message.setText(content);
        emailSender.send(message);
    }
}