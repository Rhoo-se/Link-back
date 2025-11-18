package com.golflink.golflink.controller;

import com.golflink.golflink.dto.LoginRequestDto;
import com.golflink.golflink.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        String token = adminService.login(dto.getUsername(), dto.getPassword());
        return ResponseEntity.ok(token);
    }
}