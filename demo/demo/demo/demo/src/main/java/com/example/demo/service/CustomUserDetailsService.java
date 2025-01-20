package com.example.demo.service;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        UserEntity userData=userRepository.findByUserEmail(userEmail);

        if (userData == null) {
            // 사용자 정보가 없을 경우 예외 발생
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }

        // UserDetails에 담아서 반환
        return new CustomUserDetails(userData);

//        if(userData != null){
//            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
//            return new CustomUserDetails(userData);
//
//        }
//        return null;
    }
}
