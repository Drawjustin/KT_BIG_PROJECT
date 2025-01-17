package com.example.demo.dto;

import com.example.demo.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//DB내용 전달
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity; //userEntity 초기화

    public CustomUserDetails(UserEntity userEntity){
        this.userEntity=userEntity;
    }

    //role값 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection=new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getUserRole();
            }
        });
        return collection;
    }

    //password 리턴
    @Override
    public String getPassword() {
        return userEntity.getUserPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUserEmail();
    }

    //계정이 만료되지 않았는지 확인
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    //계정이 잠기지 않았는지 확인
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    //자격 증명이 만료되지 않았는지 확인
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    // 계정이 활성 상태인지 확인
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
