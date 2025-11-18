package com.golflink.golflink.controller;
import com.golflink.golflink.dto.ReservationResponseDto;
import com.golflink.golflink.service.ReservationService;
import com.golflink.golflink.domain.Reservation;
import com.golflink.golflink.dto.ReservationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Arrays; // 이제 Arrays는 필요 없으므로 삭제 가능

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<String>> getReservedSlotsByDate(@RequestParam("date") String dateString, @RequestParam("proId") Long proId) {
        LocalDate date = LocalDate.parse(dateString);
        List<String> reservedSlots = reservationService.getReservedSlotsByDate(date, proId);
        return ResponseEntity.ok(reservedSlots);
    }

    @PostMapping
// ResponseEntity의 제네릭 타입을 ReservationResponseDto로 변경
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto) {
        // Service로부터 이제 DTO를 직접 받음
        ReservationResponseDto createdDto = reservationService.createReservation(requestDto);
        return ResponseEntity.status(201).body(createdDto);
    }
}