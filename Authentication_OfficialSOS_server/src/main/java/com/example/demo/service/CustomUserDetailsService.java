package com.example.demo.service;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//DB에서 사용자 정보 로드
@Service
public class CustomUserDetailsService implements UserDetailsService {

    //DB접근할 수 있는 userRepository
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository){

        this.memberRepository=memberRepository;
    }

    //이메일 확인
    @Override
    public UserDetails loadUserByUsername (String memberEmail) throws UsernameNotFoundException{

        //DB에 userEmail 조회
        Optional<Member> memberOptional = memberRepository.findByMemberEmail(memberEmail);
        if (!memberOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + memberEmail);
        }
        Member memberData = memberOptional.get();

        // UserDetails에 담아서 반환
        return new CustomUserDetails(memberData);

    }
}
