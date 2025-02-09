package com.example.demo.config;

import com.example.demo.jwt.JWTFilter;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final AuthService authService;
    private final MemberRepository memberRepository;

    private final UserRepository userRepository;
    public SecurityConfig(JWTUtil jwtUtil,AuthService authService, UserRepository userRepository, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.authService=authService;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }


    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter(jwtUtil, authService,userRepository, memberRepository);
    }

    // AuthenticationManager 설정 추가
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 전역적으로 CSRF 비활성화
                .csrf(csrf -> csrf.disable())
                // 불필요한 formLogin과 httpBasic 비활성화
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .authorizeHttpRequests((auth) -> auth
                        //authentication
                        .requestMatchers("/error", "/api/login","/api/join","/api/reissue","/api/logout").permitAll()
                        .requestMatchers("/api/departments", "/api/teams").permitAll()
                        .requestMatchers("/db-test").permitAll()
                        //complaint
                        .requestMatchers("/complaints/public/**").permitAll() // 공개 민원 엔드포인트
 //                       .requestMatchers("/complaints").hasRole("USER") // 민원 등록, 수정, 삭제
                        .requestMatchers("/complaints/{complaintSeq}").hasRole("USER") // 단건 조회

                        //others
                        .requestMatchers("/admin").hasRole("ADMIN") // 관리자 페이지
                        .requestMatchers("/user/**").hasRole("USER")  // 사용자만 접근 가능
                        .requestMatchers("/reissue").permitAll() // 토큰 refresh 발급
                        .anyRequest().authenticated()); // 나머지 요청은 인증 필요

        // JWT 필터 설정
        http
                .addFilterBefore(jwtFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        // 세션 관리 설정
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CORS 설정 추가
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "https://www.officialsos.shop",
                "http://localhost:5173",
                "http://localhost:5174",
                "https://officialsos.shop"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}