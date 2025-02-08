package com.example.demo.config;

import com.example.demo.jwt.JWTFilter;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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

import java.util.Arrays;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthService authService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final JwtConfig jwtConfig;

    public SecurityConfig(
            @Lazy AuthService authService,
            AuthenticationConfiguration authenticationConfiguration,
            JWTUtil jwtUtil,
            JwtConfig jwtConfig) {
        this.authService = authService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.jwtConfig=jwtConfig;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter(jwtUtil, authService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .formLogin((auth)-> auth.disable())
                .httpBasic((auth)-> auth.disable())
                .authorizeHttpRequests((auth)-> auth
                        .requestMatchers("/error","/minwon/","/minwon/api/healthy","/minwon/api/login","/minwon/api/join","/minwon/api/reissue","/minwon/api/logout").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")//관리자만 접근가능
                        .requestMatchers("/user/**").hasRole("USER")  // 사용자만 접근 가능
                        .requestMatchers("/reissue").permitAll() //토큰 refresh 발급
                        .anyRequest().authenticated()); // 나머지 요청은 인증 필요

        // JWT 필터 설정
        http
                .addFilterBefore(jwtFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        // 세션 관리 설정
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // CORS 설정 메서드 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "https://www.officialsos.shop",
                "https://officialsos.shop",
                "https://14ff-121-136-132-253.ngrok-free.app"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
