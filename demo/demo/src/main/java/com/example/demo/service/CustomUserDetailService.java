package com.example.demo.service;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    //DB접근할 수 있는 userRepository
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    //이메일 확인
    @Override
    public UserDetails loadUserByUsername (String userEmail) throws UsernameNotFoundException{

        //DB 조회
        UserEntity userData=userRepository.findByUserEmail(userEmail);

        if(userData != null){
            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);

        }
        return null;
    }
}
