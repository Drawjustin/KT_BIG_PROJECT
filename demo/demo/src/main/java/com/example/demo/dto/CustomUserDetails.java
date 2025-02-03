package com.example.demo.dto;

import com.example.demo.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user; //userEntity 초기화

    public User getUser() {
        return user;
    }

    public CustomUserDetails(User user){
        this.user=user;
    }

    //userEmail 리턴
    @Override
    public String getUsername() {

        return user.getUserEmail();
    }

    //password 리턴
    @Override
    public String getPassword() {

        return user.getUserPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ArrayList 직접 생성 대신 List.of 사용
        return List.of(new SimpleGrantedAuthority(user.getUserRole()));
    }

}
