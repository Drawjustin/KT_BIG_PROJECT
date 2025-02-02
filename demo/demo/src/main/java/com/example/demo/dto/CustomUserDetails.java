package com.example.demo.dto;

import com.example.demo.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity; //userEntity 초기화

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public CustomUserDetails(UserEntity userEntity){
        this.userEntity=userEntity;
    }

    //userEmail 리턴
    @Override
    public String getUsername() {

        return userEntity.getUserEmail();
    }

    //password 리턴
    @Override
    public String getPassword() {

        return userEntity.getUserPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ArrayList 직접 생성 대신 List.of 사용
        return List.of(new SimpleGrantedAuthority(userEntity.getUserRole()));
    }

    //계정이 만료되지 않았는지 확인
    @Override
    public boolean isAccountNonExpired() {
        return !userEntity.getIsDeleted();  // BaseEntity의 삭제 상태 활용
    }

    //계정이 잠기지 않았는지 확인
    @Override
    public boolean isAccountNonLocked() {
        return !userEntity.getIsDeleted();  // BaseEntity의 삭제 상태 활용
    }

    //자격 증명이 만료되지 않았는지 확인
    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 필요에 따라 비밀번호 만료 정책 구현
    }

    // 계정이 활성 상태인지 확인
    @Override
    public boolean isEnabled() {
        return !userEntity.getIsDeleted();  // BaseEntity의 삭제 상태 활용
    }
}

