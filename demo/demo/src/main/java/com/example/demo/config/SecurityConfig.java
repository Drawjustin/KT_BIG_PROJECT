package com.example.demo.config;

import com.example.demo.jwt.JWTUtil;
import com.example.demo.jwt.LoginFilter;
import io.jsonwebtoken.Jwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final JwtProperties jwtProperties;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, JwtProperties jwtProperties) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil=jwtUtil;
        this.jwtProperties=jwtProperties;
    }

    //AuthenticationManager Bean 등록, 이메일과 비밀번호 검증->유효 시 authentication객체 리턴
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http

                .csrf(csrf -> csrf.disable())
                .csrf((auth)-> auth.disable())
                .formLogin((auth)-> auth.disable())
                .httpBasic((auth)-> auth.disable())
                .authorizeHttpRequests((auth)-> auth
                        .requestMatchers("/", "/home", "/signup","/api/**","/login").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")//관리자만 접근가능
                        .requestMatchers("/user/**").hasRole("USER")  // 사용자만 접근 가능
                        .anyRequest().authenticated()); // 나머지 요청은 인증 필요

        //필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil,jwtProperties), UsernamePasswordAuthenticationFilter.class);


        http
                .sessionManagement((session)-> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
