package com.golflink.golflink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/") // 루트 경로("/") 요청을 받으면
    public ResponseEntity<String> healthCheck() {
        // "서버 정상"이라는 간단한 200 OK 응답을 보냅니다.
        return ResponseEntity.ok("Server is healthy!");
    }
}