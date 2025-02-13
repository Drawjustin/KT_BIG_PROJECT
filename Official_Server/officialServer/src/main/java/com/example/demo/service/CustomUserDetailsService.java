package com.example.demo.service;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//DB에서 사용자 정보 로드
@Service
public class CustomUserDetailsService implements UserDetailsService {

    //DB접근할 수 있는 userRepository
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){

        this.userRepository=userRepository;
    }

    //이메일 확인
    @Override
    public UserDetails loadUserByUsername (String userEmail) throws UsernameNotFoundException{

        //DB에 userEmail 조회
        Optional<User> userOptional = userRepository.findByUserEmail(userEmail);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }
        User userData = userOptional.get();

        // UserDetails에 담아서 반환
        return new CustomUserDetails(userData);

    }
}
