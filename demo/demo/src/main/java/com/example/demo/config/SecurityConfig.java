package com.example.demo.config;

import com.example.demo.jwt.JWTFilter;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.RefreshRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AccessTokenService;
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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Collections;
import jakarta.servlet.http.HttpServletRequest;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccessTokenService accessTokenService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;

    public SecurityConfig(
            AccessTokenService accessTokenService,
            AuthenticationConfiguration authenticationConfiguration,
            JWTUtil jwtUtil,
            UserRepository userRepository,
            RefreshRepository refreshRepository) {
        this.accessTokenService = accessTokenService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.refreshRepository = refreshRepository;
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
                .formLogin((auth)-> auth.disable())
                .httpBasic((auth)-> auth.disable())
                .authorizeHttpRequests((auth)-> auth
                        .requestMatchers("/api/login","/api/join","/api/reissue","/api/logout").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")//관리자만 접근가능
                        .requestMatchers("/user/**").hasRole("USER")  // 사용자만 접근 가능
                        .requestMatchers("/reissue").permitAll() //토큰 refresh 발급
                        .anyRequest().authenticated()); // 나머지 요청은 인증 필요

        // JWT 필터 설정
        http
                .addFilterBefore(new JWTFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);

        // 세션 관리 설정
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
