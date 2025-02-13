package com.example.demo.service;

import com.example.demo.config.JwtConfig;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.request.JoinRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.*;
import com.example.demo.entity.Department;
import com.example.demo.entity.Refresh;
import com.example.demo.entity.Team;
import com.example.demo.entity.User;
import com.example.demo.exception.*;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final TeamRepository teamRepository;
    private final DepartmentRepository departmentRepository;

    private static final String ACCESS_TOKEN_PREFIX = "access_token:";
    private final DistrictRepository districtRepository;

    public AuthService(
            JWTUtil jwtUtil,
            RedisTemplate<String, String> redisTemplate,
            RefreshRepository refreshRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Lazy AuthenticationManager authenticationManager,
            JwtConfig jwtConfig,
            TeamRepository teamRepository,
            DepartmentRepository departmentRepository,
            DistrictRepository districtRepository) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.refreshRepository = refreshRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtConfig=jwtConfig;
        this.teamRepository=teamRepository;
        this.departmentRepository=departmentRepository;
        this.districtRepository = districtRepository;
    }

    // 회원가입 처리
    public void join(JoinRequest request) {
        log.info("Join Request - TeamSeq: {}", request.teamSeq());

        List<Team> allTeams = teamRepository.findAll();
        log.info("Total Teams Count: {}", allTeams.size());
        allTeams.forEach(team ->
                log.info("Team - ID: {}, Name: {}, Department: {}",
                        team.getTeamSeq(),
                        team.getTeamName(),
                        team.getDepartment() != null ? team.getDepartment().getDepartmentName() : "No Department")
        );

        // null 체크 추가
        if (request.teamSeq() == null) {
            throw new IllegalArgumentException("Team ID is required");
        }

        if (userRepository.existsByUserEmail(request.userEmail())) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        // 팀 존재 여부 확인
        Team team = teamRepository.findById(request.teamSeq())
                .orElseThrow(() -> {
                    log.error("Team not found - TeamSeq: {}", request.teamSeq());
                    return new TeamNotFoundException("존재하지 않는 팀입니다.");
                });

        log.info("Found Team - TeamName: {}", team.getTeamName());
        // 사용자 생성 및 저장
        User user = request.toEntity(passwordEncoder, team);
        userRepository.save(user);
    }

    // 로그인 처리
    public AuthResponse login(LoginRequest request) {
        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.userEmail(), request.userPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUserEntity();

        // Access Token 생성
        String accessToken = createAccessToken(userDetails);

        // Refresh Token 생성 및 저장
        String refreshToken = createRefreshToken(userDetails);
        saveRefreshToken(user, refreshToken);

        return new AuthResponse(user.getUserEmail(), accessToken, refreshToken);
    }

    // 로그아웃 처리
    public void logout(String userEmail, String accessToken) {
        // Access Token 삭제
        deleteAccessToken(userEmail);

        // 블랙리스트에 Access Token 추가
        blacklistToken(accessToken);

        // Refresh Token 삭제
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        refreshRepository.deleteByUser(user);
    }

    // 토큰 재발급
    public TokenResponse reissueToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("유효하지 않은 refresh token입니다.");
        }

        String userEmail = jwtUtil.getUserEmail(refreshToken);
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 새로운 Access Token 발급
        String newAccessToken = createAccessToken(userDetails);

        // Refresh Token 재발급 (선택적)
        String newRefreshToken = createRefreshToken(userDetails);
        saveRefreshToken(user, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    // Access Token 관련 메서드
    private String createAccessToken(CustomUserDetails userDetails) {
        String token = jwtUtil.createJwt(userDetails, "access", jwtConfig.getAccessTokenExpiration());
        saveAccessToken(userDetails.getUsername(), token);
        return token;
    }

    //Access토큰 redis에 저장
    private void saveAccessToken(String userEmail, String token) {
        String key = ACCESS_TOKEN_PREFIX + userEmail;
        redisTemplate.opsForValue().set(key, token, jwtConfig.getAccessTokenExpiration(), TimeUnit.MILLISECONDS);
    }

    private void deleteAccessToken(String userEmail) {
        redisTemplate.delete(ACCESS_TOKEN_PREFIX + userEmail);
    }

    // Refresh토큰 mysql에 저장
    private String createRefreshToken(CustomUserDetails userDetails) {
        return jwtUtil.createJwt(userDetails, "refresh", jwtConfig.getRefreshTokenExpiration());
    }

    private void saveRefreshToken(User user, String token) {
        Refresh refreshToken = refreshRepository.findByUser(user);
        if (refreshToken != null) {
            refreshToken.updateToken(token,
                    new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration()));
        } else {
            refreshToken = new Refresh(
                    user,
                    token,
                    new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration())
            );
        }
        refreshRepository.save(refreshToken);
    }

    public boolean validateAccessToken(String userEmail, String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + userEmail;
        String storedToken = redisTemplate.opsForValue().get(key);
        return storedToken != null && storedToken.equals(accessToken);
    }

    private boolean validateRefreshToken(String token) {
        return token != null && !jwtUtil.isExpired(token) &&
                jwtUtil.getCategory(token).equals("refresh");
    }

    // 토큰을 블랙리스트에 추가
    public void blacklistToken(String token) {
        // 토큰의 남은 유효 시간 동안 블랙리스트에 보관
        long remainingTime = jwtUtil.getRemainingTime(token);

        redisTemplate.opsForValue().set(
                "blacklist:" + token,
                "true",
                Duration.ofMillis(remainingTime)
        );
    }

    public List<DistrictResponse> getAllDistricts(){
        return districtRepository.findAll().stream()
                .map(dept -> new DistrictResponse(
                        dept.getDistrictSeq(),
                        dept.getDistrictName()
                )).collect(Collectors.toList());
    }



    // 부서 목록 조회 메서드 추가
    public List<DepartmentResponse> getDepartmentsByDistrict(Long districtSeq) {
        return departmentRepository.findAllByDistrictDistrictSeq(districtSeq).stream()
                .map(department -> new DepartmentResponse(
                        department.getDepartmentSeq(),
                        department.getDepartmentName()
                ))
                .collect(Collectors.toList());

    }

    // 특정 부서의 팀 목록 조회 메서드 추가
    public List<TeamResponse> getTeamsByDepartment(Long departmentSeq) {
        Department department = departmentRepository.findById(departmentSeq)
                .orElseThrow(() -> new TeamNotFoundException("존재하지 않는 부서입니다."));

        return teamRepository.findByDepartment(department).stream()
                .map(team -> new TeamResponse(
                        team.getTeamSeq(),
                        team.getTeamName(),
                        department.getDepartmentSeq()
                ))
                .collect(Collectors.toList());
    }

    // 토큰이 블랙리스트에 있는지 확인
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey("blacklist:" + token)
        );
    }


}