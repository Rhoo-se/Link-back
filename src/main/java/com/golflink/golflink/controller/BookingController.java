package com.golflink.golflink.controller;

import com.golflink.golflink.dto.BookingInfoResponseDto;
import com.golflink.golflink.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * 예약 페이지에 필요한 정보를 조회하는 API
     * @param proId 프론트엔드로부터 받은 프로의 ID
     * @return 예약 페이지 정보가 담긴 DTO
     */
    @CrossOrigin
    @GetMapping("/api/booking/info")
    public ResponseEntity<BookingInfoResponseDto> getBookingInfo(@RequestParam Long proId) {
        // 1. proId를 Service에 전달하여 비즈니스 로직을 수행합니다.
        BookingInfoResponseDto bookingInfo = bookingService.getBookingInfo(proId);

        // 2. Service로부터 받은 DTO를 성공 응답(200 OK)과 함께 반환합니다.
        return ResponseEntity.ok(bookingInfo);
    }
}