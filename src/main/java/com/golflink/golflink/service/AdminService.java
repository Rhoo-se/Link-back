package com.golflink.golflink.service;

import com.golflink.golflink.domain.Admin;
import com.golflink.golflink.jwt.JwtTokenProvider;
import com.golflink.golflink.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections; // Collections import 확인

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 사용자입니다."));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // admin.getRole() (String)을 List<String>으로 변환하여 전달합니다.
        return jwtTokenProvider.createToken(admin.getUsername(), Collections.singletonList(admin.getRole()));
    }
}